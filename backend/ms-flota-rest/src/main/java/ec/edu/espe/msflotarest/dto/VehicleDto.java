package ec.edu.espe.msflotarest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import ec.edu.espe.msflotarest.entity.VehicleStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VehicleDto {
    private UUID id;

    @NotBlank(message = "La matrícula es obligatoria")
    @Size(min = 2, max = 20, message = "La matrícula debe tener entre 2 y 20 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9\\-]+$", message = "La matrícula solo permite letras, números y guiones")
    private String plate;

    @NotBlank(message = "El tipo de vehículo es obligatorio")
    @Pattern(regexp = "^(Moto|Auto|Furgoneta|Camion)$", message = "Tipo debe ser: Moto, Auto, Furgoneta o Camion")
    private String type;

    @NotNull(message = "La capacidad es obligatoria")
    @Positive(message = "La capacidad debe ser un valor positivo")
    @Max(value = 50000, message = "La capacidad máxima es 50000 kg")
    private Double capacityKg;

    @Positive(message = "La autonomía debe ser un valor positivo")
    @Max(value = 5000, message = "La autonomía máxima es 5000 km")
    private Double autonomyKm;

    @NotNull(message = "El estado es obligatorio")
    private VehicleStatus status;

    private String maintenanceOrderCode;
}
