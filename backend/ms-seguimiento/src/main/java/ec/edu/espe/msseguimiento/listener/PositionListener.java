package ec.edu.espe.msseguimiento.listener;

import ec.edu.espe.msseguimiento.config.RabbitConfig;
import ec.edu.espe.msseguimiento.dto.PositionEvent;
import ec.edu.espe.msseguimiento.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PositionListener {

    private final TrackingService trackingService;

    @RabbitListener(queues = RabbitConfig.TRACKING_QUEUE)
    public void handlePositionUpdate(PositionEvent event) {
        log.debug("Received position update for shipment: {}", event.getShipmentId());
        trackingService.broadcastPosition(event);
    }
}
