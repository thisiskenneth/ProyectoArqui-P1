package ec.edu.espe.msauth.service;

import ec.edu.espe.msauth.dto.AuthRequest;
import ec.edu.espe.msauth.dto.AuthResponse;
import ec.edu.espe.msauth.dto.RegisterRequest;
import ec.edu.espe.msauth.entity.Role;
import ec.edu.espe.msauth.entity.User;
import ec.edu.espe.msauth.repository.UserRepository;
import ec.edu.espe.msauth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public User register(RegisterRequest request) {
        // Regla de negocio: no permitir username duplicado
        if (userRepository.existsByUsernameIgnoreCase(request.getUsername())) {
            throw new IllegalStateException("El nombre de usuario '" + request.getUsername() + "' ya está en uso.");
        }
        // Regla de negocio: no permitir email duplicado
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalStateException("El email '" + request.getEmail() + "' ya está registrado.");
        }
        // Regla de seguridad: el endpoint público solo puede registrar clientes y conductores
        Role role = (request.getRole() != null) ? request.getRole() : Role.CLIENTE;
        if (role == Role.ADMIN) {
            throw new IllegalStateException("No está permitido registrar usuarios con rol ADMIN desde este endpoint.");
        }

        User user = User.builder()
                .username(request.getUsername().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail().toLowerCase())
                .roles(Set.of(role))
                .build();

        User saved = userRepository.save(user);
        log.info("Usuario registrado: {} con rol: {}", saved.getUsername(), role);
        return saved;
    }

    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsernameIgnoreCase(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Intento de login fallido para usuario: {}", request.getUsername());
            // Mensaje genérico para no revelar si el usuario existe
            throw new IllegalArgumentException("Credenciales inválidas.");
        }

        String token = jwtUtil.generateToken(user.getUsername(),
                user.getRoles().stream().map(Enum::name).collect(Collectors.toSet()));

        log.info("Login exitoso: {}", user.getUsername());
        return new AuthResponse(token, user.getUsername(),
                user.getRoles().stream().map(Enum::name).collect(Collectors.toSet()));
    }

    @Transactional(readOnly = true)
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}
