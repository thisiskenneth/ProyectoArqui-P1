package ec.edu.espe.msflotarest.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String plate;

    private String type;
    private Double capacityKg;
    private Double autonomyKm;

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;
}
