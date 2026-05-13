package ec.edu.espe.msflotarest.controller;

import ec.edu.espe.msflotarest.client.MaintenanceTallerClient;
import ec.edu.espe.msflotarest.client.dto.TallerOrdenResponse;
import ec.edu.espe.msflotarest.client.dto.TallerVehiculoResponse;
import ec.edu.espe.msflotarest.dto.MaintenanceInfoDto;
import ec.edu.espe.msflotarest.dto.VehicleDto;
import ec.edu.espe.msflotarest.dto.request.MaintenanceOrderRequest;
import ec.edu.espe.msflotarest.dto.response.MaintenanceOrderResponse;
import ec.edu.espe.msflotarest.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles/{id}")
@RequiredArgsConstructor
public class VehicleMaintenanceController {

    private final VehicleService vehicleService;
    private final MaintenanceTallerClient maintenanceTallerClient;

    @GetMapping("/maintenance")
    public ResponseEntity<MaintenanceInfoDto> getMaintenanceInfo(@PathVariable UUID id) {
        VehicleDto vehicle = vehicleService.findById(id);
        TallerVehiculoResponse tallerResponse = maintenanceTallerClient.consultar(vehicle.getPlate());
        return ResponseEntity.ok(new MaintenanceInfoDto(
                tallerResponse.getMatricula(),
                tallerResponse.getEstado(),
                tallerResponse.getUltimoMantenimiento(),
                tallerResponse.getObservaciones()));
    }

    @PostMapping("/maintenance-orders")
    public ResponseEntity<MaintenanceOrderResponse> createMaintenanceOrder(
            @PathVariable UUID id,
            @Valid @RequestBody MaintenanceOrderRequest body) {
        VehicleDto vehicle = vehicleService.findById(id);
        TallerOrdenResponse tallerResponse = maintenanceTallerClient.registrar(
                vehicle.getPlate(), body.getDescripcion());
        MaintenanceOrderResponse response = new MaintenanceOrderResponse(
                tallerResponse.getCodigoOrden(),
                tallerResponse.getFechaIngreso(),
                tallerResponse.getMensaje());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
