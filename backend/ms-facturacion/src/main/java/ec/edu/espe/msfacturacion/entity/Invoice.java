package ec.edu.espe.msfacturacion.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String invoiceNumber;

    @Column(nullable = false)
    private String orderId;

    private String clientId;
    private Double weightKg;
    private String geographicLevel; // LOCAL, PROVINCIAL, NATIONAL
    private String vehicleType;

    private Double baseRate;
    private Double weightSurcharge;
    private Double vehicleAdjustment;
    private Double totalAmount;

    private String status; // PAID, PENDING
    private LocalDateTime issuedAt;

    @PrePersist
    protected void onCreate() {
        issuedAt = LocalDateTime.now();
    }
}
