package ec.edu.espe.mspedidos.dto.request;

import ec.edu.espe.mspedidos.enums.Prioridad;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoCreateRequest {

    @NotNull(message = "El ID del cliente es obligatorio")
    private UUID clienteId;

    @NotBlank(message = "El origen es obligatorio")
    private String origen;

    @NotBlank(message = "El destino es obligatorio")
    private String destino;

    @NotNull(message = "El peso total es obligatorio")
    @Positive(message = "El peso total debe ser mayor a 0")
    private BigDecimal peso;

    private Prioridad prioridad;

    private String notas;

    @NotEmpty(message = "Debe haber al menos 1 paquete en el pedido")
    @Valid
    private List<PaqueteRequest> paquetes;
}
