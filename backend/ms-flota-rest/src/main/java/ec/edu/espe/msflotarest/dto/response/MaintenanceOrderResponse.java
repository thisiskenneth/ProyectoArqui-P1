package ec.edu.espe.msflotarest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceOrderResponse {
    private String codigoOrden;
    private String fechaIngreso;
    private String mensaje;
}
