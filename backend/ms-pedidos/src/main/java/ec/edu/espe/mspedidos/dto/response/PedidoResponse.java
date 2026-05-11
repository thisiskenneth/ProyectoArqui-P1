package ec.edu.espe.mspedidos.dto.response;

import ec.edu.espe.mspedidos.enums.EstadoPedido;
import ec.edu.espe.mspedidos.enums.Prioridad;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponse {
    private UUID id;
    private UUID clienteId;
    private String origen;
    private String destino;
    private BigDecimal peso;
    private EstadoPedido estado;
    private Prioridad prioridad;
    private String notas;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PaqueteResponse> paquetes;
}
