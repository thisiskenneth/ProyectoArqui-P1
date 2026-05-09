package ec.edu.espe.msnotificaciones.service;

import ec.edu.espe.msnotificaciones.dto.OrderEvent;
import ec.edu.espe.msnotificaciones.dto.ShipmentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void processOrderCreated(OrderEvent event) {
        log.info("NOTIFICACION - Pedido Creado: ID={}, Cliente={}",
            event.getOrderId(), event.getCustomerEmail());
    }

    public void processOrderCancelled(OrderEvent event) {
        log.info("NOTIFICACION - Pedido Cancelado: ID={}, Cliente={}",
            event.getOrderId(), event.getCustomerEmail());
    }

    public void processShipmentAssigned(ShipmentEvent event) {
        log.info("NOTIFICACION - Envio Asignado: ID={}, Pedido={}, Conductor={}, Placa={}, Cliente={}",
            event.getShipmentId(), event.getOrderId(), event.getDriverName(), event.getPlate(), event.getCustomerEmail());
    }

    public void processOrderDelivered(OrderEvent event) {
        log.info("NOTIFICACION - Pedido Entregado: ID={}, Cliente={}",
            event.getOrderId(), event.getCustomerEmail());
    }
}
