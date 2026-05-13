package ec.edu.espe.msruteo.controller;

import ec.edu.espe.msruteo.dto.AssignmentRequest;
import ec.edu.espe.msruteo.dto.response.ShipmentResponse;
import ec.edu.espe.msruteo.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping("/assign")
    public ResponseEntity<ShipmentResponse> assign(@Valid @RequestBody AssignmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shipmentService.assignShipment(request));
    }

    @GetMapping
    public ResponseEntity<List<ShipmentResponse>> getAll() {
        return ResponseEntity.ok(shipmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(shipmentService.findById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<ShipmentResponse>> getByOrderId(@PathVariable UUID orderId) {
        return ResponseEntity.ok(shipmentService.findByOrderId(orderId));
    }
}
