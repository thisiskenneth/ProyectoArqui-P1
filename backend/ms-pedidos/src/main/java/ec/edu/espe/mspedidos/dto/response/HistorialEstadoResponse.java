package ec.edu.espe.mspedidos.dto.response;

import ec.edu.espe.mspedidos.enums.EstadoPedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialEstadoResponse {
    private UUID id;
    private EstadoPedido estadoAnterior;
    private EstadoPedido estadoNuevo;
    private UUID usuarioId;
    private String observaciones;
    private LocalDateTime fecha;
}
