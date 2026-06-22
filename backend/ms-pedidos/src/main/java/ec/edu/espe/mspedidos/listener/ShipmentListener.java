package ec.edu.espe.mspedidos.listener;

import ec.edu.espe.mspedidos.config.RabbitConfig;
import ec.edu.espe.mspedidos.dto.ShipmentEvent;
import ec.edu.espe.mspedidos.entity.Order;
import ec.edu.espe.mspedidos.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShipmentListener {

    private final OrderRepository orderRepository;

    @RabbitListener(queues = RabbitConfig.SHIPMENT_ASSIGNED_QUEUE)
    public void handleShipmentAssigned(ShipmentEvent event) {
        log.info("Evento envio.asignado recibido para pedido: {}", event.getOrderId());
        
        try {
            Long orderId = Long.parseLong(event.getOrderId());
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            
            if (!"ASSIGNED".equals(order.getStatus()) && !"DELIVERED".equals(order.getStatus())) {
                order.setStatus("ASSIGNED");
                orderRepository.save(order);
                log.info("Estado del pedido {} actualizado a ASSIGNED", orderId);
            }
        } catch (Exception e) {
            log.error("Error al actualizar estado del pedido {}: {}", event.getOrderId(), e.getMessage());
        }
    }
}
