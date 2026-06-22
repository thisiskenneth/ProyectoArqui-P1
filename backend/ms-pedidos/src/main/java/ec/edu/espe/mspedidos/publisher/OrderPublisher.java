package ec.edu.espe.mspedidos.publisher;

import ec.edu.espe.mspedidos.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishOrderCreated(Map<String, Object> event) {
        log.info("Publishing order created event with routing key: pedido.creado");
        rabbitTemplate.convertAndSend(RabbitConfig.LOGIFLOW_EXCHANGE, "pedido.creado", event);
    }

    public void publishOrderCancelled(Map<String, Object> event) {
        log.info("Publishing order cancelled event with routing key: pedido.cancelado");
        rabbitTemplate.convertAndSend(RabbitConfig.LOGIFLOW_EXCHANGE, "pedido.cancelado", event);
    }

    public void publishOrderDelivered(Map<String, Object> event) {
        log.info("Publishing order delivered event with routing key: pedido.entregado");
        rabbitTemplate.convertAndSend(RabbitConfig.LOGIFLOW_EXCHANGE, "pedido.entregado", event);
    }
}
