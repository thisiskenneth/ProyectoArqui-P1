package ec.edu.espe.mspedidos.controller;

import ec.edu.espe.mspedidos.dto.OrderRequest;
import ec.edu.espe.mspedidos.dto.OrderResponse;
import ec.edu.espe.mspedidos.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAll() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(
            @PathVariable @Positive(message = "El id del pedido debe ser un entero positivo") Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping("/active/client/{clientId}")
    public ResponseEntity<List<OrderResponse>> getActiveByClient(
            @PathVariable @NotBlank(message = "El clientId es obligatorio") String clientId) {
        return ResponseEntity.ok(orderService.findActiveByCliente(clientId));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.save(request));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancel(
            @PathVariable @Positive(message = "El id del pedido debe ser un entero positivo") Long id) {
        return ResponseEntity.ok(orderService.cancel(id));
    }

    @PostMapping("/{id}/deliver")
    public ResponseEntity<OrderResponse> deliver(
            @PathVariable @Positive(message = "El id del pedido debe ser un entero positivo") Long id) {
        return ResponseEntity.ok(orderService.markAsDelivered(id));
    }
}
