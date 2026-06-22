package ec.edu.espe.msruteo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String orderId;

    private String vehicleId;
    private String vehiclePlate;
    
    private String origin;
    private String destination;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    private LocalDateTime assignedAt;
}
