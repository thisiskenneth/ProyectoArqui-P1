package ec.edu.espe.msflotarest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceInfoDto {
    private String plate;
    private String status;
    private String lastMaintenance;
    private String notes;
}
