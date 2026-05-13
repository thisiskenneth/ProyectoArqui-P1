package ec.edu.espe.mstallersoap.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MantenimientoResponse {
    private String codigoOrden;
    private String fechaIngreso;
    private String mensaje;
}
