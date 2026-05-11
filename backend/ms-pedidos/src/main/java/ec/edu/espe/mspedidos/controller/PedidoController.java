package ec.edu.espe.mspedidos.controller;

import ec.edu.espe.mspedidos.dto.request.CambiarEstadoRequest;
import ec.edu.espe.mspedidos.dto.request.PedidoCreateRequest;
import ec.edu.espe.mspedidos.dto.response.HistorialEstadoResponse;
import ec.edu.espe.mspedidos.dto.response.PedidoResponse;
import ec.edu.espe.mspedidos.services.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponse> crearPedido(@Valid @RequestBody PedidoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crearPedido(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> obtenerPedidoPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(pedidoService.obtenerPedidoPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<PedidoResponse>> listarPedidos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(pedidoService.listarPedidos(PageRequest.of(page, size)));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Page<PedidoResponse>> listarPedidosPorCliente(
            @PathVariable UUID clienteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(pedidoService.listarPedidosPorCliente(clienteId, PageRequest.of(page, size)));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<PedidoResponse> cambiarEstado(
            @PathVariable UUID id,
            @Valid @RequestBody CambiarEstadoRequest request) {
        return ResponseEntity.ok(pedidoService.cambiarEstado(id, request));
    }

    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialEstadoResponse>> obtenerHistorial(@PathVariable UUID id) {
        return ResponseEntity.ok(pedidoService.obtenerHistorial(id));
    }
}
