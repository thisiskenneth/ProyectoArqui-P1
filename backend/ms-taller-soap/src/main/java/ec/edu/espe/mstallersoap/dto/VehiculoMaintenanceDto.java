package ec.edu.espe.mstallersoap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehiculoMaintenanceDto {
    private String matricula;
    private String estado;
    private String ultimoMantenimiento;
    private String observaciones;
}
