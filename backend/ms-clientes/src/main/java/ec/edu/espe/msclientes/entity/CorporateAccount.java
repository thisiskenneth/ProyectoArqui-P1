package ec.edu.espe.msclientes.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "corporate_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CorporateAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    /** RUC ecuatoriano: exactamente 13 dígitos */
    @Column(nullable = false, unique = true, length = 13)
    private String ruc;

    @Column(nullable = false, length = 200)
    private String businessName;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal creditLimit;

    @Column(length = 100)
    private String industry;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
