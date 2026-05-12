package ec.edu.espe.mstaller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaintenanceResponse {
    private String codigoOrden;
    private String fechaIngreso;
    private String mensaje;
}
