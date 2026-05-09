package ec.edu.espe.msfacturacion.listener;

import ec.edu.espe.msfacturacion.config.RabbitConfig;
import ec.edu.espe.msfacturacion.dto.OrderEvent;
import ec.edu.espe.msfacturacion.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class InvoiceListener {

    private final InvoiceService invoiceService;

    @RabbitListener(queues = RabbitConfig.BILLING_QUEUE)
    public void handleOrderDelivered(OrderEvent event) {
        log.info("Evento pedido.entregado recibido para el pedido: {}", event.getOrderId());
        invoiceService.createInvoiceFromEvent(event);
    }
}
