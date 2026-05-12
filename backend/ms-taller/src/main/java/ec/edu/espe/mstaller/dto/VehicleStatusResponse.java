package ec.edu.espe.mstaller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehicleStatusResponse {
    private String matricula;
    private String estado;
    private String ultimoMantenimiento;
    private String observaciones;
}
