package ec.edu.espe.msauth.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    @Pattern(
        regexp = "^[a-zA-Z0-9._-]+$",
        message = "El nombre de usuario solo puede contener letras, números, puntos, guiones bajos y guiones"
    )
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._-])[A-Za-z\\d@$!%*?&._-]{8,}$",
        message = "La contraseña debe contener al menos: una mayúscula, una minúscula, un dígito y un carácter especial (@$!%*?&._-)"
    )
    private String password;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener formato válido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    private String email;

    /**
     * Si no se especifica, se asigna el rol CLIENTE por defecto en el servicio.
     * No se acepta el rol ADMIN desde este endpoint público.
     */
    private ec.edu.espe.msauth.entity.Role role;
}
