package ec.edu.espe.msauth.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserCreateRequest {

    // ── Credenciales ──────────────────────────────────────────────────────────

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener formato válido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._-])[A-Za-z\\d@$!%*?&._-]{8,}$",
        message = "La contraseña debe tener al menos: una mayúscula, una minúscula, un número y un carácter especial"
    )
    private String password;

    /**
     * Nombre de rol a asignar (ej: CLIENTE, CONDUCTOR).
     * No se permite ADMIN desde este endpoint.
     */
    @NotBlank(message = "El rol inicial es obligatorio")
    @Pattern(regexp = "^[A-Z_]+$", message = "El rol debe estar en mayúsculas")
    private String roleName;

    // ── Datos personales (Person) ─────────────────────────────────────────────

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 80, message = "El nombre debe tener entre 2 y 80 caracteres")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "El nombre solo puede contener letras y espacios")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
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

    /**
     * Número de cédula ecuatoriana (10 dígitos) o pasaporte (hasta 20 chars).
     */
    @Pattern(
        regexp = "^[0-9A-Za-z]{6,20}$",
        message = "El documento debe tener entre 6 y 20 caracteres alfanuméricos"
    )
    private String documentNumber;

    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate birthDate;
}
