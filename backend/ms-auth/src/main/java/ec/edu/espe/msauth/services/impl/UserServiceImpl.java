package ec.edu.espe.msauth.services.impl;

import ec.edu.espe.msauth.dto.request.UserCreateRequest;
import ec.edu.espe.msauth.dto.request.UserUpdateRequest;
import ec.edu.espe.msauth.dto.response.PersonResponse;
import ec.edu.espe.msauth.dto.response.RoleResponse;
import ec.edu.espe.msauth.dto.response.UserResponse;
import ec.edu.espe.msauth.entity.Person;
import ec.edu.espe.msauth.entity.RoleEntity;
import ec.edu.espe.msauth.entity.User;
import ec.edu.espe.msauth.entity.UserRole;
import ec.edu.espe.msauth.repository.RoleRepository;
import ec.edu.espe.msauth.repository.UserRepository;
import ec.edu.espe.msauth.repository.UserRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    // ── Consultas ─────────────────────────────────────────────────────────────

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse findById(UUID id) {
        return toResponse(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id)));
    }

    public UserResponse findByUsername(String username) {
        return toResponse(userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + username)));
    }

    // ── Creación ──────────────────────────────────────────────────────────────

    @Transactional
    public UserResponse create(UserCreateRequest request) {
        // Validaciones de unicidad de email
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalStateException("El email '" + request.getEmail() + "' ya está registrado.");
        }

        // Regla de seguridad: no crear ADMIN desde endpoint general
        if ("ADMIN".equalsIgnoreCase(request.getRoleName())) {
            throw new IllegalStateException("No está permitido crear usuarios con rol ADMIN desde este endpoint.");
        }

        // Buscar rol (case-insensitive: "cliente" == "CLIENTE")
        RoleEntity role = roleRepository.findByNameIgnoreCase(request.getRoleName())
                .orElseThrow(() -> new EntityNotFoundException(
                    "Rol no encontrado: '" + request.getRoleName() + "'. Crea el rol primero en /api/roles"));

        // Generar username automático: fn + mn + ln
        String username = generarUsername(request.getFirstName(), request.getMiddleName(), request.getLastName());

        // La cédula/documento es la contraseña inicial
        String passwordHash = passwordEncoder.encode(request.getDocumentNumber());

        // Crear usuario
        User user = User.builder()
                .username(username)
                .passwordHash(passwordHash)
                .email(request.getEmail().toLowerCase())
                .active(true)
                .build();
        User savedUser = userRepository.save(user);

        // Crear Person vinculada (UUID compartido via @MapsId)
        Person person = Person.builder()
                .id(savedUser.getId())
                .user(savedUser)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .email(request.getEmail().toLowerCase())
                .documentNumber(request.getDocumentNumber())
                .birthDate(request.getBirthDate())
                .build();
        savedUser.setPerson(person);
        userRepository.save(savedUser);

        // Asignar rol inicial
        UserRole userRole = UserRole.builder()
                .user(savedUser)
                .role(role)
                .build();
        userRoleRepository.save(userRole);

        log.info("Usuario creado: '{}' con rol: '{}' (contraseña = cédula)", savedUser.getUsername(), role.getName());
        return toResponse(userRepository.findById(savedUser.getId()).orElse(savedUser));
    }

    // ── Actualización ─────────────────────────────────────────────────────────

    @Transactional
    public UserResponse update(UUID id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));

        // Actualizar contraseña si se provee (se sigue permitiendo cambio manual)
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        }

        // Activar/desactivar cuenta
        if (request.getActive() != null) {
            user.setActive(request.getActive());
            log.info("Cuenta {} {}: {}", user.getUsername(),
                    request.getActive() ? "activada" : "desactivada", id);
        }

        // Actualizar Person si existe (o crear datos personales si no tiene)
        Person person = user.getPerson();
        if (person == null) {
            person = Person.builder()
                    .id(user.getId())
                    .user(user)
                    .build();
        }
        if (request.getFirstName() != null && !request.getFirstName().isBlank())
            person.setFirstName(request.getFirstName());
        if (request.getLastName() != null && !request.getLastName().isBlank())
            person.setLastName(request.getLastName());
        if (request.getPhone() != null)      person.setPhone(request.getPhone());
        if (request.getAddress() != null)    person.setAddress(request.getAddress());
        if (request.getBirthDate() != null)  person.setBirthDate(request.getBirthDate());
        user.setPerson(person);

        return toResponse(userRepository.save(user));
    }

    // ── Asignación de roles ───────────────────────────────────────────────────

    @Transactional
    public UserResponse assignRole(UUID userId, UUID roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + userId));
        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con id: " + roleId));

        if (userRoleRepository.existsByUserIdAndRoleId(userId, roleId)) {
            throw new IllegalStateException("El usuario ya tiene asignado el rol: " + role.getName());
        }

        UserRole userRole = UserRole.builder().user(user).role(role).build();
        userRoleRepository.save(userRole);
        log.info("Rol '{}' asignado a usuario '{}'", role.getName(), user.getUsername());
        return toResponse(userRepository.findById(userId).orElse(user));
    }

    @Transactional
    public UserResponse removeRole(UUID userId, UUID roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + userId));

        if (!userRoleRepository.existsByUserIdAndRoleId(userId, roleId)) {
            throw new EntityNotFoundException("El usuario no tiene asignado ese rol.");
        }

        // Regla: no dejar al usuario sin roles
        List<UserRole> currentRoles = userRoleRepository.findByUserId(userId);
        if (currentRoles.size() <= 1) {
            throw new IllegalStateException("No se puede quitar el último rol del usuario.");
        }

        userRoleRepository.deleteByUserIdAndRoleId(userId, roleId);
        log.info("Rol {} removido de usuario '{}'", roleId, user.getUsername());
        // Recargar para reflejar el cambio
        return toResponse(userRepository.findById(userId).orElseThrow());
    }

    // ── Eliminación ───────────────────────────────────────────────────────────

    @Transactional
    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
        userRepository.delete(user);
        log.info("Usuario eliminado: {}", user.getUsername());
    }

    // ── Generación de Username ────────────────────────────────────────────────

    /**
     * Genera username como: primera letra del nombre + primera letra del segundo nombre (si existe)
     * + primer apellido completo + primera letra del segundo apellido (si existe).
     *
     * Ejemplos:
     *   fn="Ana", mn=null,   ln="García"        → "anagarcia"
     *   fn="Ana", mn="María",ln="García"         → "amgarcia"
     *   fn="Ana", mn="María",ln="García López"   → "amgarcial"
     *
     * Si el username ya existe, agrega sufijo numérico: "amgarcial", "amgarcial1", "amgarcial2"...
     */
    private String generarUsername(String fn, String mn, String ln) {
        if (fn == null || fn.trim().isEmpty() || ln == null || ln.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Nombres y apellidos son obligatorios para generar el usuario");
        }

        StringBuilder sb = new StringBuilder();

        // Primera letra del primer nombre
        sb.append(fn.trim().toLowerCase().charAt(0));

        // Primera letra del segundo nombre (si existe)
        if (mn != null && !mn.trim().isEmpty()) {
            sb.append(mn.trim().toLowerCase().charAt(0));
        }

        // Primer apellido completo
        String[] surnames = ln.trim().split("\\s+");
        sb.append(surnames[0].toLowerCase());

        // Primera letra del segundo apellido (si existe)
        if (surnames.length > 1 && !surnames[1].isEmpty()) {
            sb.append(surnames[1].toLowerCase().charAt(0));
        }

        String baseUsername = sb.toString()
                .replaceAll("[^a-z0-9]", ""); // elimina caracteres no ASCII

        String finalUsername = baseUsername;
        int count = 1;
        while (userRepository.findByUsernameIgnoreCase(finalUsername).isPresent()) {
            finalUsername = baseUsername + count;
            count++;
        }

        return finalUsername;
    }

    // ── Mapeador ─────────────────────────────────────────────────────────────

    private UserResponse toResponse(User u) {
        PersonResponse personResponse = null;
        if (u.getPerson() != null) {
            Person p = u.getPerson();
            personResponse = PersonResponse.builder()
                    .id(p.getId())
                    .firstName(p.getFirstName())
                    .lastName(p.getLastName())
                    .fullName(p.getFullName())
                    .phone(p.getPhone())
                    .address(p.getAddress())
                    .email(p.getEmail())
                    .documentNumber(p.getDocumentNumber())
                    .birthDate(p.getBirthDate())
                    .createdAt(p.getCreatedAt())
                    .updatedAt(p.getUpdatedAt())
                    .build();
        }

        List<RoleResponse> roles = userRoleRepository.findByUserId(u.getId()).stream()
                .map(ur -> RoleResponse.builder()
                        .id(ur.getRole().getId())
                        .name(ur.getRole().getName())
                        .description(ur.getRole().getDescription())
                        .createdAt(ur.getRole().getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return UserResponse.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .active(u.getActive())
                .person(personResponse)
                .roles(roles)
                .createdAt(u.getCreatedAt())
                .updatedAt(u.getUpdatedAt())
                .build();
    }
}
