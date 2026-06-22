package ec.edu.espe.mstallersoap.controller;

import ec.edu.espe.mstallersoap.dto.VehiculoMaintenanceDto;
import ec.edu.espe.mstallersoap.dto.request.MantenimientoRequest;
import ec.edu.espe.mstallersoap.dto.response.MantenimientoResponse;
import ec.edu.espe.mstallersoap.service.MaintenanceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"", "/api"})
@RequiredArgsConstructor
public class MaintenanceRestController {

    private final MaintenanceService maintenanceService;

    @GetMapping("/vehiculos/{matricula}")
    public ResponseEntity<VehiculoMaintenanceDto> consultarVehiculo(
            @PathVariable
            @NotBlank(message = "La matricula es obligatoria")
            @Size(min = 2, max = 20, message = "La matricula debe tener entre 2 y 20 caracteres") String matricula) {
        return ResponseEntity.ok(maintenanceService.consultar(matricula));
    }

    @PostMapping("/mantenimientos")
    public ResponseEntity<MantenimientoResponse> registrarMantenimiento(@Valid @RequestBody MantenimientoRequest request) {
        return new ResponseEntity<>(
                maintenanceService.registrar(request.getMatricula(), request.getDescripcion()),
                HttpStatus.CREATED);
    }
}
