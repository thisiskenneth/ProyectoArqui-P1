package ec.edu.espe.msruteo.listener;

import ec.edu.espe.msruteo.config.RabbitConfig;
import ec.edu.espe.msruteo.dto.AssignmentRequest;
import ec.edu.espe.msruteo.dto.OrderEvent;
import ec.edu.espe.msruteo.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderListener {

    private final ShipmentService shipmentService;

    @RabbitListener(queues = RabbitConfig.ORDER_CREATED_QUEUE)
    public void handleOrderCreated(OrderEvent event) {
        log.info("Evento pedido.creado recibido. Iniciando auto-asignación para: {}", event.getOrderId());
        
        AssignmentRequest request = new AssignmentRequest();
        request.setOrderId(event.getOrderId());
        request.setCustomerEmail(event.getCustomerEmail());
        request.setOrigin(event.getOrigin());
        request.setDestination(event.getDestination());
        
        try {
            shipmentService.assignShipment(request);
            log.info("Auto-asignación exitosa para pedido: {}", event.getOrderId());
        } catch (Exception e) {
            log.error("Error en auto-asignación para pedido {}: {}", event.getOrderId(), e.getMessage());
        }
    }
}
