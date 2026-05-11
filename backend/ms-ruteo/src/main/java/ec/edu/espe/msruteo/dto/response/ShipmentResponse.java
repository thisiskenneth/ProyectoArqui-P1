package ec.edu.espe.msruteo.dto.response;

import ec.edu.espe.msruteo.entity.ShipmentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ShipmentResponse {
    private Long id;
    private String orderId;
    private Long vehicleId;
    private String vehiclePlate;
    private String origin;
    private String destination;
    private ShipmentStatus status;
    private LocalDateTime assignedAt;
}
