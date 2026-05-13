package ec.edu.espe.msflotarest.controller;

import ec.edu.espe.msflotarest.dto.MaintenanceInfoDto;
import ec.edu.espe.msflotarest.dto.VehicleDto;
import ec.edu.espe.msflotarest.dto.request.MaintenanceOrderRequest;
import ec.edu.espe.msflotarest.dto.response.MaintenanceOrderResponse;
import ec.edu.espe.msflotarest.service.VehicleService;
import ec.edu.espe.msflotarest.soap.MaintenanceSoapClient;
import ec.edu.espe.msflotarest.soap.model.ConsultarVehiculoResponse;
import ec.edu.espe.msflotarest.soap.model.RegistrarOrdenMantenimientoResponse;
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
    private final MaintenanceSoapClient maintenanceSoapClient;

    @GetMapping("/maintenance")
    public ResponseEntity<MaintenanceInfoDto> getMaintenanceInfo(@PathVariable UUID id) {
        VehicleDto vehicle = vehicleService.findById(id);
        ConsultarVehiculoResponse soapResponse = maintenanceSoapClient.consultar(vehicle.getPlate());
        return ResponseEntity.ok(new MaintenanceInfoDto(
                soapResponse.getMatricula(),
                soapResponse.getEstado(),
                soapResponse.getUltimoMantenimiento(),
                soapResponse.getObservaciones()));
    }

    @PostMapping("/maintenance-orders")
    public ResponseEntity<MaintenanceOrderResponse> createMaintenanceOrder(
            @PathVariable UUID id,
            @Valid @RequestBody MaintenanceOrderRequest body) {
        VehicleDto vehicle = vehicleService.findById(id);
        RegistrarOrdenMantenimientoResponse soapResponse = maintenanceSoapClient.registrar(
                vehicle.getPlate(), body.getDescripcion());
        MaintenanceOrderResponse response = new MaintenanceOrderResponse(
                soapResponse.getCodigoOrden(),
                soapResponse.getFechaIngreso(),
                soapResponse.getMensaje());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
