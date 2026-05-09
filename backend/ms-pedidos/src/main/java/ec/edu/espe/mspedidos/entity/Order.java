package ec.edu.espe.mspedidos.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clienteId;

    private String customerEmail; // For notifications and billing

    @ElementCollection
    private List<String> items;

    private Double total;
    private Double weightKg;
    private String geographicLevel; // LOCAL, PROVINCIAL, NATIONAL
    private String vehicleType;

    private String origin;
    private String destination;

    private String status; // CREATED, IN_TRANSIT, DELIVERED, CANCELLED
    private LocalDateTime createdAt;
}
