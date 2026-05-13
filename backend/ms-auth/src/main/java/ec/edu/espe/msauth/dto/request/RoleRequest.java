package ec.edu.espe.msauth.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RoleRequest {

    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Pattern(
        regexp = "^[A-Z_]+$",
        message = "El nombre del rol debe estar en mayúsculas y solo puede contener letras y guiones bajos (ej: ADMIN, SUPER_ADMIN)"
    )
    private String name;

    @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
    private String description;
}
