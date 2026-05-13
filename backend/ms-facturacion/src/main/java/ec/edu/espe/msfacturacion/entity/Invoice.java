package ec.edu.espe.msfacturacion.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, unique = true, length = 30)
    private String invoiceNumber;

    @Column(nullable = false, updatable = false)
    private UUID orderId;

    @Column(nullable = false)
    private String clientId;

    @Column(nullable = false)
    private Double weightKg;

    @Column(nullable = false, length = 30)
    private String geographicLevel;

    @Column(nullable = false, length = 30)
    private String vehicleType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal baseRate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal weightSurcharge;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal vehicleAdjustment;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime issuedAt;

    @PrePersist
    protected void onCreate() {
        issuedAt = LocalDateTime.now();
        if (status == null) status = InvoiceStatus.PENDING;
    }
}
