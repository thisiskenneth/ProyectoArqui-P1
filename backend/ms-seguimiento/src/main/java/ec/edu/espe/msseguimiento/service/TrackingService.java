package ec.edu.espe.msseguimiento.service;

import ec.edu.espe.msseguimiento.dto.PositionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackingService {

    private final SimpMessagingTemplate messagingTemplate;

    public void broadcastPosition(PositionEvent event) {
        log.info("📍 Retransmitiendo posición: Shipment={}, Lat={}, Lng={}", 
            event.getShipmentId(), event.getLat(), event.getLng());
        
        // Enviamos al tópico específico del envío para que los clientes suscritos lo reciban
        messagingTemplate.convertAndSend("/topic/shipment/" + event.getShipmentId(), event);
        
        // También podemos enviar a un tópico general por pedido
        messagingTemplate.convertAndSend("/topic/order/" + event.getOrderId(), event);
    }
}
