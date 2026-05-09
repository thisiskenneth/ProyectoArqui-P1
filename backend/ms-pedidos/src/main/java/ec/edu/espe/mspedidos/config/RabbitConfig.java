package ec.edu.espe.mspedidos.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String LOGIFLOW_EXCHANGE = "logiflow.exchange";
    public static final String SHIPMENT_ASSIGNED_QUEUE = "logiflow.pedidos.shipment.assigned.queue";

    @Bean
    public Queue shipmentAssignedQueue() {
        return new Queue(SHIPMENT_ASSIGNED_QUEUE, true);
    }

    @Bean
    public TopicExchange logiflowExchange() {
        return new TopicExchange(LOGIFLOW_EXCHANGE);
    }

    @Bean
    public Binding shipmentAssignedBinding(Queue shipmentAssignedQueue, TopicExchange logiflowExchange) {
        return BindingBuilder.bind(shipmentAssignedQueue).to(logiflowExchange).with("envio.asignado");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
