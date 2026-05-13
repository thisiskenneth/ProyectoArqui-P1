package ec.edu.espe.msflotarest.controller;

import ec.edu.espe.msflotarest.dto.FleetAvailabilityDto;
import ec.edu.espe.msflotarest.service.DriverService;
import ec.edu.espe.msflotarest.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fleet")
@RequiredArgsConstructor
public class AvailabilityController {
    private final VehicleService vehicleService;
    private final DriverService driverService;

    @GetMapping("/availability")
    public ResponseEntity<FleetAvailabilityDto> getAvailabilityForRouting() {
        return ResponseEntity.ok(new FleetAvailabilityDto(
                vehicleService.findAvailable(),
                driverService.findAvailable()
        ));
    }
}
