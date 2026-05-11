package ec.edu.espe.mspedidos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaqueteRequest {

    @NotBlank(message = "La descripción del paquete es obligatoria")
    private String descripcion;

    @Positive(message = "El peso del paquete debe ser mayor a 0")
    private BigDecimal peso;

    private String dimensiones;

    @Builder.Default
    private Boolean fragil = false;
}
