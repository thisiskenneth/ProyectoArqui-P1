package ec.edu.espe.graphqlgateway.listener;

import ec.edu.espe.graphqlgateway.config.RabbitConfig;
import ec.edu.espe.graphqlgateway.dto.PositionEvent;
import ec.edu.espe.graphqlgateway.service.TrackingPositionStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PositionListener {

    private final TrackingPositionStore trackingPositionStore;

    @RabbitListener(queues = RabbitConfig.GATEWAY_TRACKING_QUEUE)
    public void handlePositionUpdate(PositionEvent event) {
        trackingPositionStore.save(event);
        log.debug("Cached latest position for shipment {}", event.getShipmentId());
    }
}
