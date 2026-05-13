package ec.edu.espe.msflotarest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FleetAvailabilityDto {
    private List<VehicleDto> vehicles;
    private List<DriverDto> drivers;
}
