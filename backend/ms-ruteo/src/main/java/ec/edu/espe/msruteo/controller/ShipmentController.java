package ec.edu.espe.msruteo.controller;

import ec.edu.espe.msruteo.dto.AssignmentRequest;
import ec.edu.espe.msruteo.dto.response.ShipmentResponse;
import ec.edu.espe.msruteo.service.ShipmentService;
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
    public ResponseEntity<ShipmentResponse> assign(@RequestBody AssignmentRequest request) {
        return new ResponseEntity<>(shipmentService.assignShipment(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ShipmentResponse>> getAll() {
        return ResponseEntity.ok(shipmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(shipmentService.findById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<ShipmentResponse>> getByOrderId(@PathVariable String orderId) {
        return ResponseEntity.ok(shipmentService.findByOrderId(orderId));
    }
}
