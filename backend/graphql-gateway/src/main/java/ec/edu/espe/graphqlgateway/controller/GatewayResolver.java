package ec.edu.espe.graphqlgateway.controller;

import ec.edu.espe.graphqlgateway.dto.OrderDto;
import ec.edu.espe.graphqlgateway.service.GatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class GatewayResolver {

    private final GatewayService gatewayService;

    @QueryMapping
    public List<OrderDto> pedidosActivos(@Argument("clienteId") String clienteId) {
        return gatewayService.getPedidosActivos(clienteId);
    }

    @QueryMapping
    public Map<String, Object> envio(@Argument("id") String id) {
        return gatewayService.getEnvioAgregado(id);
    }

    @MutationMapping
    public OrderDto crearPedido(@Argument("input") OrderDto input) {
        return gatewayService.crearPedido(input);
    }

    @MutationMapping
    public OrderDto cancelarPedido(@Argument("id") String id) {
        return gatewayService.cancelarPedido(id);
    }
}
