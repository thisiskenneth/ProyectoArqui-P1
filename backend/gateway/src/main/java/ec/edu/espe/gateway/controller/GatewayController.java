package ec.edu.espe.gateway.controller;

import ec.edu.espe.gateway.client.OrderClient;
import ec.edu.espe.gateway.dto.OrderDto;
import ec.edu.espe.gateway.dto.PedidoInput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gateway")
@RequiredArgsConstructor
public class GatewayController {

    private final OrderClient orderClient;

    @GetMapping("/pedidos")
    public ResponseEntity<List<OrderDto>> getPedidosActivos(@RequestParam String clienteId) {
        List<OrderDto> pedidos = orderClient.getActiveOrders(clienteId);
        return ResponseEntity.ok(pedidos);
    }

    @PostMapping("/pedidos")
    public ResponseEntity<OrderDto> crearPedido(@RequestBody PedidoInput input) {
        OrderDto orderDto = new OrderDto();
        orderDto.setClienteId(input.getClienteId());
        orderDto.setCustomerEmail(input.getCustomerEmail());
        orderDto.setItems(input.getItems());
        orderDto.setTotal(input.getTotal());
        orderDto.setOrigin(input.getOrigin());
        orderDto.setDestination(input.getDestination());
        orderDto.setWeightKg(input.getWeightKg());
        orderDto.setGeographicLevel(input.getGeographicLevel());
        orderDto.setVehicleType(input.getVehicleType());
        
        OrderDto response = orderClient.createOrder(orderDto);
        return ResponseEntity.ok(response);
    }
}
