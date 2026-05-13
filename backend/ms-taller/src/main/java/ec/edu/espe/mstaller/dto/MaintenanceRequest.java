package ec.edu.espe.mstaller.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MaintenanceRequest {

    /**
     * Matrícula ecuatoriana del vehículo a revisar.
     * Formato: 3 letras + 3 o 4 dígitos (ej: ABC-1234).
     */
    @NotBlank(message = "La matrícula es obligatoria")
    @Pattern(
        regexp = "^[A-Z]{3}-?[0-9]{3,4}[A-Z]?$",
        message = "La matrícula debe tener formato ecuatoriano válido (ej: ABC-1234)"
    )
    private String matricula;

    @NotBlank(message = "La descripción de la orden es obligatoria")
    @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres")
    private String descripcion;
}
