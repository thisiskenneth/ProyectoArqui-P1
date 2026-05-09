package ec.edu.espe.msclientes.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "corporate_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CorporateAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ruc;

    @Column(nullable = false)
    private String businessName;

    private Double creditLimit;
    private String industry;
}
