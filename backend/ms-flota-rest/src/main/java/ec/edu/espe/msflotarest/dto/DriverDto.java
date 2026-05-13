package ec.edu.espe.msflotarest.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class DriverDto {
    private UUID id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String lastName;

    @NotBlank(message = "El número de licencia es obligatorio")
    @Size(min = 5, max = 30, message = "La licencia debe tener entre 5 y 30 caracteres")
    private String licenseNumber;

    @Pattern(regexp = "^(\\+?[0-9\\-\\s]{7,20})?$", message = "Formato de teléfono inválido")
    private String phone;

    @NotNull(message = "El estado de disponibilidad es obligatorio")
    private Boolean available;
}
