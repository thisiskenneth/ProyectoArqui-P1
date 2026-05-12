package ec.edu.espe.mstaller.controller;

import ec.edu.espe.mstaller.dto.MaintenanceRequest;
import ec.edu.espe.mstaller.dto.MaintenanceResponse;
import ec.edu.espe.mstaller.dto.VehicleStatusResponse;
import ec.edu.espe.mstaller.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping("/vehicles/{matricula}")
    public VehicleStatusResponse consultarVehiculo(@PathVariable String matricula) {
        return maintenanceService.consultar(matricula);
    }

    @PostMapping("/orders")
    public MaintenanceResponse registrarOrden(@RequestBody MaintenanceRequest request) {
        return maintenanceService.registrar(request.getMatricula(), request.getDescripcion());
    }
}
