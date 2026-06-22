package ec.edu.espe.msauth.service;

import ec.edu.espe.msauth.dto.AuthRequest;
import ec.edu.espe.msauth.dto.AuthResponse;
import ec.edu.espe.msauth.dto.ClienteCreatedEvent;
import ec.edu.espe.msauth.dto.RegisterRequest;
import ec.edu.espe.msauth.entity.Role;
import ec.edu.espe.msauth.entity.User;
import ec.edu.espe.msauth.repository.UserRepository;
import ec.edu.espe.msauth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public User register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .roles(request.getRoles())
                .build();
        return userRepository.save(user);
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUsername(), 
                user.getRoles().stream().map(Enum::name).collect(Collectors.toSet()));

        return new AuthResponse(token, user.getUsername(), 
                user.getRoles().stream().map(Enum::name).collect(Collectors.toSet()));
    }

    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    /**
     * Provisiona el usuario de acceso (rol CLIENT) a partir del evento cliente.creado.
     * El email es el username. Idempotente: si ya existe o no viene password, no hace nada.
     */
    public void provisionClientUser(ClienteCreatedEvent event) {
        if (event.getPassword() == null || event.getPassword().isBlank()) {
            log.info("Cliente {} sin password: no se provisiona acceso", event.getEmail());
            return;
        }
        if (userRepository.findByUsername(event.getEmail()).isPresent()
                || userRepository.findByEmail(event.getEmail()).isPresent()) {
            log.info("El usuario {} ya existe: se omite provisionamiento", event.getEmail());
            return;
        }
        User user = User.builder()
                .username(event.getEmail())
                .password(passwordEncoder.encode(event.getPassword()))
                .email(event.getEmail())
                .roles(Set.of(Role.CLIENT))
                .build();
        userRepository.save(user);
        log.info("Usuario de acceso creado para cliente {} (rol CLIENT)", event.getEmail());
    }
}
