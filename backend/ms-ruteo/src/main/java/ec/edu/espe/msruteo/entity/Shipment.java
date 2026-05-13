package ec.edu.espe.msruteo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private UUID orderId;

    @Column(nullable = false)
    private Long vehicleId;

    @Column(nullable = false, length = 20)
    private String vehiclePlate;

    @Column(nullable = false, length = 300)
    private String origin;

    @Column(nullable = false, length = 300)
    private String destination;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShipmentStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime assignedAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (assignedAt == null) assignedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
