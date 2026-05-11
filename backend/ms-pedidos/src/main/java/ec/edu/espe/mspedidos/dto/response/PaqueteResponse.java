package ec.edu.espe.mspedidos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaqueteResponse {
    private UUID id;
    private String descripcion;
    private BigDecimal peso;
    private String dimensiones;
    private Boolean fragil;
    private LocalDateTime createdAt;
}
