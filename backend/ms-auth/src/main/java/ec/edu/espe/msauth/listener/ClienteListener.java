package ec.edu.espe.msauth.listener;

import ec.edu.espe.msauth.config.RabbitConfig;
import ec.edu.espe.msauth.dto.ClienteCreatedEvent;
import ec.edu.espe.msauth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClienteListener {

    private final AuthService authService;

    @RabbitListener(queues = RabbitConfig.CLIENTE_CREATED_QUEUE)
    public void handleClienteCreado(ClienteCreatedEvent event) {
        log.info("Evento cliente.creado recibido para email={}", event.getEmail());
        try {
            authService.provisionClientUser(event);
        } catch (Exception e) {
            log.error("No se pudo provisionar el usuario para {}: {}", event.getEmail(), e.getMessage());
        }
    }
}
