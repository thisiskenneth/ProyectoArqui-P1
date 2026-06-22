package ec.edu.espe.msruteo.controller;

import ec.edu.espe.msruteo.dto.AssignmentRequest;
import ec.edu.espe.msruteo.dto.response.ShipmentResponse;
import ec.edu.espe.msruteo.service.ShipmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping("/assign")
    public ResponseEntity<ShipmentResponse> assign(@Valid @RequestBody AssignmentRequest request) {
        return new ResponseEntity<>(shipmentService.assignShipment(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ShipmentResponse>> getAll() {
        return ResponseEntity.ok(shipmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponse> getById(
            @PathVariable @Positive(message = "El id del envio debe ser un entero positivo") Long id) {
        return ResponseEntity.ok(shipmentService.findById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<ShipmentResponse>> getByOrderId(
            @PathVariable @NotBlank(message = "El orderId es obligatorio") String orderId) {
        return ResponseEntity.ok(shipmentService.findByOrderId(orderId));
    }
}
