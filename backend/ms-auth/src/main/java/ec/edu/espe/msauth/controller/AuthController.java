package ec.edu.espe.msauth.controller;

import ec.edu.espe.msauth.dto.AuthRequest;
import ec.edu.espe.msauth.dto.AuthResponse;
import ec.edu.espe.msauth.dto.RegisterRequest;
import ec.edu.espe.msauth.entity.User;
import ec.edu.espe.msauth.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verify(@RequestParam @NotBlank(message = "El token es obligatorio") String token) {
        return ResponseEntity.ok(authService.validateToken(token));
    }
}
