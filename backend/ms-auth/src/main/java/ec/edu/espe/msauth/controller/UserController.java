package ec.edu.espe.msauth.controller;

import ec.edu.espe.msauth.dto.request.UserCreateRequest;
import ec.edu.espe.msauth.dto.request.UserUpdateRequest;
import ec.edu.espe.msauth.dto.response.UserResponse;
import ec.edu.espe.msauth.services.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    /** Asignar rol existente a un usuario */
    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<UserResponse> assignRole(
            @PathVariable UUID userId,
            @PathVariable UUID roleId) {
        return ResponseEntity.ok(userService.assignRole(userId, roleId));
    }

    /** Revocar rol de un usuario */
    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<UserResponse> removeRole(
            @PathVariable UUID userId,
            @PathVariable UUID roleId) {
        return ResponseEntity.ok(userService.removeRole(userId, roleId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
