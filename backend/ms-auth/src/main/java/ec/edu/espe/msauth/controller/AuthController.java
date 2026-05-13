package ec.edu.espe.msauth.controller;

import ec.edu.espe.msauth.dto.AuthRequest;
import ec.edu.espe.msauth.dto.AuthResponse;
import ec.edu.espe.msauth.dto.request.UserCreateRequest;
import ec.edu.espe.msauth.dto.response.UserResponse;
import ec.edu.espe.msauth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /** Registro público — devuelve el UserResponse (con person y rol) */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    /** Login — devuelve JWT + username + roles */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /** Validar token JWT */
    @PostMapping("/verify")
    public ResponseEntity<Boolean> verify(@RequestParam String token) {
        return ResponseEntity.ok(authService.validateToken(token));
    }
}
