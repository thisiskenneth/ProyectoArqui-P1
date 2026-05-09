package ec.edu.espe.msruteo.controller;

import ec.edu.espe.msruteo.dto.AssignmentRequest;
import ec.edu.espe.msruteo.entity.Shipment;
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
    public ResponseEntity<Shipment> assign(@RequestBody AssignmentRequest request) {
        return new ResponseEntity<>(shipmentService.assignShipment(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Shipment>> getAll() {
        return ResponseEntity.ok(shipmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shipment> getById(@PathVariable Long id) {
        return ResponseEntity.ok(shipmentService.findById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Shipment>> getByOrderId(@PathVariable String orderId) {
        return ResponseEntity.ok(shipmentService.findByOrderId(orderId));
    }
}
