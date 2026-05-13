package ec.edu.espe.msflotarest.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TallerVehiculoResponse {
    private String matricula;
    private String estado;
    private String ultimoMantenimiento;
    private String observaciones;
}
