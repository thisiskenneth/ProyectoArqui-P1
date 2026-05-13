package ec.edu.espe.msflotarest.dto.request;

import ec.edu.espe.msflotarest.entity.VehicleStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class VehicleRequest {

    /**
     * Matrícula ecuatoriana: 3 letras + 3 o 4 dígitos (ej: ABC-1234).
     * También acepta placas de uso oficial o especial.
     */
    @NotBlank(message = "La matrícula es obligatoria")
    @Pattern(
        regexp = "^[A-Z]{3}-?[0-9]{3,4}[A-Z]?$",
        message = "La matrícula debe tener el formato ecuatoriano válido (ej: ABC-1234)"
    )
    @Size(max = 10, message = "La matrícula no puede superar los 10 caracteres")
    private String plate;

    @NotBlank(message = "El tipo de vehículo es obligatorio")
    @Pattern(
        regexp = "^(MOTO|AUTO|FURGONETA|CAMION)$",
        message = "El tipo debe ser: MOTO, AUTO, FURGONETA o CAMION"
    )
    private String type;

    @NotNull(message = "La capacidad en kg es obligatoria")
    @DecimalMin(value = "1.0", message = "La capacidad mínima es 1 kg")
    @DecimalMax(value = "20000.0", message = "La capacidad máxima es 20,000 kg")
    private Double capacityKg;

    @NotNull(message = "La autonomía en km es obligatoria")
    @DecimalMin(value = "10.0", message = "La autonomía mínima es 10 km")
    @DecimalMax(value = "5000.0", message = "La autonomía máxima es 5,000 km")
    private Double autonomyKm;

    @NotNull(message = "El estado del vehículo es obligatorio")
    private VehicleStatus status;
}
