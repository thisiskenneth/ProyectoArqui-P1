package ec.edu.espe.msruteo.publisher;

import ec.edu.espe.msruteo.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishShipmentAssigned(Object event) {
        log.info("Publishing shipment.assigned event to RabbitMQ with routing key: envio.asignado");
        rabbitTemplate.convertAndSend(RabbitConfig.LOGIFLOW_EXCHANGE, "envio.asignado", event);
    }
}
