package ec.edu.espe.msclientes.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CorporateAccountResponse {
    private UUID id;
    private String ruc;
    private String businessName;
    private BigDecimal creditLimit;
    private String industry;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
