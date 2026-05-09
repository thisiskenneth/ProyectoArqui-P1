package ec.edu.espe.msnotificaciones.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.msnotificaciones.config.RabbitConfig;
import ec.edu.espe.msnotificaciones.dto.OrderEvent;
import ec.edu.espe.msnotificaciones.dto.ShipmentEvent;
import ec.edu.espe.msnotificaciones.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = RabbitConfig.NOTIFICATION_QUEUE)
    public void handleNotificationEvent(Map<String, Object> payload,
                                        @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        log.debug("Received event with routing key: {}", routingKey);

        try {
            if ("pedido.creado".equals(routingKey)) {
                notificationService.processOrderCreated(toOrderEvent(payload));
            } else if ("pedido.cancelado".equals(routingKey)) {
                notificationService.processOrderCancelled(toOrderEvent(payload));
            } else if ("pedido.entregado".equals(routingKey)) {
                notificationService.processOrderDelivered(toOrderEvent(payload));
            } else if ("envio.asignado".equals(routingKey)) {
                notificationService.processShipmentAssigned(toShipmentEvent(payload));
            } else {
                log.warn("Unknown routing key received: {}", routingKey);
            }
        } catch (IllegalArgumentException e) {
            log.error("Error converting payload for routing key {}: {}", routingKey, e.getMessage());
        }
    }

    private OrderEvent toOrderEvent(Map<String, Object> payload) {
        return objectMapper.convertValue(payload, OrderEvent.class);
    }

    private ShipmentEvent toShipmentEvent(Map<String, Object> payload) {
        return objectMapper.convertValue(payload, ShipmentEvent.class);
    }
}
