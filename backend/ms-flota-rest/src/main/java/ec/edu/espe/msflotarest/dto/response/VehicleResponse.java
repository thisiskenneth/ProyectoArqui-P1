package ec.edu.espe.msflotarest.dto.response;

import ec.edu.espe.msflotarest.entity.VehicleStatus;
import lombok.Data;

@Data
public class VehicleResponse {
    private Long id;
    private String plate;
    private String type;
    private Double capacityKg;
    private Double autonomyKm;
    private VehicleStatus status;
}
