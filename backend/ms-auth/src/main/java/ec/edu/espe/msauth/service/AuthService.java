package ec.edu.espe.msauth.service;

import ec.edu.espe.msauth.dto.AuthRequest;
import ec.edu.espe.msauth.dto.AuthResponse;
import ec.edu.espe.msauth.dto.request.UserCreateRequest;
import ec.edu.espe.msauth.dto.response.UserResponse;
import ec.edu.espe.msauth.entity.User;
import ec.edu.espe.msauth.repository.UserRepository;
import ec.edu.espe.msauth.security.JwtUtil;
import ec.edu.espe.msauth.services.impl.UserServiceImpl;
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
    private final UserServiceImpl userServiceImpl;

    /**
     * Registro público: delega en UserServiceImpl para crear el usuario completo
     * con datos personales y rol inicial.
     */
    @Transactional
    public UserResponse register(UserCreateRequest request) {
        return userServiceImpl.create(request);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsernameWithRoles(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas."));

        if (!user.getActive()) {
            throw new IllegalStateException("La cuenta está deshabilitada. Contacte al administrador.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            log.warn("Intento de login fallido para usuario: {}", request.getUsername());
            // Mensaje genérico para no revelar si el usuario existe
            throw new IllegalArgumentException("Credenciales inválidas.");
        }

        Set<String> roleNames = user.getUserRoles().stream()
                .map(ur -> ur.getRole().getName())
                .collect(Collectors.toSet());

        String token = jwtUtil.generateToken(user.getUsername(), roleNames);
        log.info("Login exitoso: {}", user.getUsername());
        return new AuthResponse(token, user.getUsername(), roleNames);
    }

    @Transactional(readOnly = true)
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}
