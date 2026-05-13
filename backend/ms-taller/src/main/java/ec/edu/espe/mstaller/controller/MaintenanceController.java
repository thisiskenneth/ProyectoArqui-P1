package ec.edu.espe.mstaller.controller;

import ec.edu.espe.mstaller.dto.MaintenanceRequest;
import ec.edu.espe.mstaller.dto.MaintenanceResponse;
import ec.edu.espe.mstaller.dto.VehicleStatusResponse;
import ec.edu.espe.mstaller.service.MaintenanceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
@Validated
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping("/vehicles/{matricula}")
    public ResponseEntity<VehicleStatusResponse> consultarVehiculo(
            @PathVariable
            @Pattern(
                regexp = "^[A-Z]{3}-?[0-9]{3,4}[A-Z]?$",
                message = "La matrícula debe tener formato ecuatoriano válido (ej: ABC-1234)"
            )
            String matricula) {
        return ResponseEntity.ok(maintenanceService.consultar(matricula));
    }

    @PostMapping("/orders")
    public ResponseEntity<MaintenanceResponse> registrarOrden(
            @Valid @RequestBody MaintenanceRequest request) {
        return ResponseEntity.ok(
                maintenanceService.registrar(request.getMatricula(), request.getDescripcion()));
    }
}
