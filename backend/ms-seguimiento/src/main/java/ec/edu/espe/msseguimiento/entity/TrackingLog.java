package ec.edu.espe.msseguimiento.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tracking_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    /** ID del envío al que pertenece este log */
    @Column(nullable = false, updatable = false)
    private UUID shipmentId;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    /** Velocidad en km/h */
    private Double speedKmh;

    /** Tiempo estimado de llegada en minutos */
    private Integer etaMinutes;

    @Column(length = 300)
    private String eventDescription;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
