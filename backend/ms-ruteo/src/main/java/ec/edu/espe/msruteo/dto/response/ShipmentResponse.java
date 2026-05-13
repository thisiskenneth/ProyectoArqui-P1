package ec.edu.espe.msruteo.dto.response;

import ec.edu.espe.msruteo.entity.ShipmentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ShipmentResponse {
    private UUID id;
    private UUID orderId;
    private Long vehicleId;
    private String vehiclePlate;
    private String origin;
    private String destination;
    private ShipmentStatus status;
    private LocalDateTime assignedAt;
    private LocalDateTime updatedAt;
}
