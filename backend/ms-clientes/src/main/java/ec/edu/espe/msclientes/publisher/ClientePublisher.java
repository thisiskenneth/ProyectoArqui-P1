package ec.edu.espe.msclientes.publisher;

import ec.edu.espe.msclientes.config.RabbitConfig;
import ec.edu.espe.msclientes.dto.event.ClienteCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClientePublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishClienteCreado(ClienteCreatedEvent event) {
        log.info("Publicando evento cliente.creado para clientId={}, email={}", event.getClientId(), event.getEmail());
        rabbitTemplate.convertAndSend(
                RabbitConfig.LOGIFLOW_EXCHANGE,
                RabbitConfig.CLIENTE_CREATED_ROUTING_KEY,
                event);
    }
}
