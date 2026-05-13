package ec.edu.espe.msflotarest.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DriverRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 80, message = "El nombre debe tener entre 2 y 80 caracteres")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "El nombre solo puede contener letras y espacios")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 80, message = "El apellido debe tener entre 2 y 80 caracteres")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "El apellido solo puede contener letras y espacios")
    private String lastName;

    /**
     * Licencia de conducir profesional ecuatoriana:
     * Tipo profesional: letra + 10 dígitos (ej: E1234567890)
     */
    @NotBlank(message = "El número de licencia es obligatorio")
    @Pattern(
        regexp = "^[A-Z][0-9]{10}$",
        message = "La licencia debe tener formato profesional ecuatoriano (ej: E1234567890)"
    )
    private String licenseNumber;

    @Pattern(
        regexp = "^(\\+593|0)[0-9]{8,9}$",
        message = "El teléfono debe ser un número ecuatoriano válido (ej: 0998765432 o +593998765432)"
    )
    private String phone;

    @NotNull(message = "El estado de disponibilidad es obligatorio")
    private Boolean available;
}
