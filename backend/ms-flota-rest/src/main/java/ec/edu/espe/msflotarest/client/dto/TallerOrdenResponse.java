package ec.edu.espe.msflotarest.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TallerOrdenResponse {
    private String codigoOrden;
    private String fechaIngreso;
    private String mensaje;
}
