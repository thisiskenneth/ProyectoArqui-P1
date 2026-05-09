package ec.edu.espe.msauth.service;

import ec.edu.espe.msauth.dto.AuthRequest;
import ec.edu.espe.msauth.dto.AuthResponse;
import ec.edu.espe.msauth.dto.RegisterRequest;
import ec.edu.espe.msauth.entity.User;
import ec.edu.espe.msauth.repository.UserRepository;
import ec.edu.espe.msauth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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
}
