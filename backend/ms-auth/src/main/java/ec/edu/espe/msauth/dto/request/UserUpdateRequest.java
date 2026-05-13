package ec.edu.espe.msauth.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateRequest {

    // ── Datos personales actualizables ────────────────────────────────────────

    @Size(min = 2, max = 80, message = "El nombre debe tener entre 2 y 80 caracteres")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "El nombre solo puede contener letras y espacios")
    private String firstName;

    @Size(min = 2, max = 80, message = "El apellido debe tener entre 2 y 80 caracteres")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "El apellido solo puede contener letras y espacios")
    private String lastName;

    @Pattern(
        regexp = "^(\\+593|0)[0-9]{8,9}$",
        message = "El teléfono debe ser ecuatoriano válido (ej: 0998765432)"
    )
    private String phone;

    @Size(max = 300, message = "La dirección no puede superar los 300 caracteres")
    private String address;

    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate birthDate;

    // ── Estado de cuenta ─────────────────────────────────────────────────────

    /** Activar o desactivar la cuenta */
    private Boolean active;

    // ── Cambio de contraseña (opcional) ──────────────────────────────────────

    @Size(min = 8, max = 100, message = "La nueva contraseña debe tener entre 8 y 100 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._-])[A-Za-z\\d@$!%*?&._-]{8,}$",
        message = "La contraseña debe tener al menos: una mayúscula, una minúscula, un número y un carácter especial"
    )
    private String newPassword;
}
