package ec.edu.espe.graphqlgateway.service;

import ec.edu.espe.graphqlgateway.client.OrderClient;
import ec.edu.espe.graphqlgateway.client.ShipmentClient;
import ec.edu.espe.graphqlgateway.dto.OrderDto;
import ec.edu.espe.graphqlgateway.dto.ShipmentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GatewayService {

    private final OrderClient orderClient;
    private final ShipmentClient shipmentClient;
    private final TrackingPositionStore trackingPositionStore;

    public List<OrderDto> getPedidosActivos(String clienteId) {
        return orderClient.getActiveOrders(clienteId);
    }

    public OrderDto crearPedido(OrderDto input) {
        return orderClient.createOrder(input);
    }

    public OrderDto cancelarPedido(String id) {
        return orderClient.cancelOrder(id);
    }

    public Map<String, Object> getEnvioAgregado(String id) {
        ShipmentDto shipment = shipmentClient.getShipment(id);

        Map<String, Object> aggregated = new HashMap<>();
        aggregated.put("id", shipment.getId());
        aggregated.put("orderId", shipment.getOrderId());
        aggregated.put("vehiclePlate", shipment.getVehiclePlate());
        aggregated.put("status", shipment.getStatus());
        aggregated.put("origin", shipment.getOrigin());
        aggregated.put("destination", shipment.getDestination());
        aggregated.put("lastPosition", trackingPositionStore.findLatest(id).orElse(null));

        return aggregated;
    }
}
