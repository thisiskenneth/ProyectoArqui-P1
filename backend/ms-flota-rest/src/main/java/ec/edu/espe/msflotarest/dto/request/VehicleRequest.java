package ec.edu.espe.msflotarest.dto.request;

import ec.edu.espe.msflotarest.entity.VehicleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class VehicleRequest {
    @NotBlank(message = "Plate is mandatory")
    private String plate;

    @NotBlank(message = "Type is mandatory")
    private String type;

    @Positive(message = "Capacity must be positive")
    private Double capacityKg;

    @Positive(message = "Autonomy must be positive")
    private Double autonomyKm;

    @NotNull(message = "Status is mandatory")
    private VehicleStatus status;
}
