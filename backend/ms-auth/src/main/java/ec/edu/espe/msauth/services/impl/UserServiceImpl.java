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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // Validaciones de unicidad
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalStateException("El email '" + request.getEmail() + "' ya está registrado.");
        }

        // Regla de seguridad: no crear ADMIN desde endpoint general
        if ("ADMIN".equalsIgnoreCase(request.getRoleName())) {
            throw new IllegalStateException("No está permitido crear usuarios con rol ADMIN desde este endpoint.");
        }

        // Buscar rol
        RoleEntity role = roleRepository.findByNameIgnoreCase(request.getRoleName())
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: " + request.getRoleName()));

        // Generar username automáticamente: nombre.apellido + número si duplicado
        String baseUsername = generarUsername(request.getFirstName(), request.getLastName());

        // Crear usuario
        User user = User.builder()
                .username(baseUsername)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail().toLowerCase())
                .active(true)
                .build();
        User savedUser = userRepository.save(user);

        // Crear Person vinculada
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

        // Asignar rol
        UserRole userRole = UserRole.builder()
                .user(savedUser)
                .role(role)
                .build();
        userRoleRepository.save(userRole);

        log.info("Usuario creado: {} con rol: {}", savedUser.getUsername(), role.getName());
        return toResponse(userRepository.findById(savedUser.getId()).orElse(savedUser));
    }

    // ── Actualización ─────────────────────────────────────────────────────────

    @Transactional
    public UserResponse update(UUID id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));

        // Actualizar contraseña si se provee
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        }

        // Activar/desactivar cuenta
        if (request.getActive() != null) {
            user.setActive(request.getActive());
        }

        // Actualizar Person
        Person person = user.getPerson();
        if (person != null) {
            if (request.getFirstName() != null)  person.setFirstName(request.getFirstName());
            if (request.getLastName() != null)   person.setLastName(request.getLastName());
            if (request.getPhone() != null)      person.setPhone(request.getPhone());
            if (request.getAddress() != null)    person.setAddress(request.getAddress());
            if (request.getBirthDate() != null)  person.setBirthDate(request.getBirthDate());
        }

        log.info("Usuario actualizado: {}", user.getUsername());
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
        userRepository.findById(userId)
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
        log.info("Rol removido de usuario con id: {}", userId);
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

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Genera username como "nombre.apellido" en minúsculas.
     * Si ya existe, agrega un sufijo numérico incremental: "nombre.apellido1", "nombre.apellido2", ...
     */
    private String generarUsername(String firstName, String lastName) {
        String base = (firstName.trim().split("\\s+")[0] + "."
                + lastName.trim().split("\\s+")[0])
                .toLowerCase()
                .replaceAll("[^a-z0-9.]", "");

        String candidate = base;
        int suffix = 1;
        while (userRepository.existsByUsernameIgnoreCase(candidate)) {
            candidate = base + suffix;
            suffix++;
        }
        return candidate;
    }

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
