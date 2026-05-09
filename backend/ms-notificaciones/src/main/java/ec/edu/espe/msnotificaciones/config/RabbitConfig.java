package ec.edu.espe.msnotificaciones.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String NOTIFICATION_QUEUE = "logiflow.notifications.queue";
    public static final String LOGIFLOW_EXCHANGE = "logiflow.exchange";

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    public TopicExchange logiflowExchange() {
        return new TopicExchange(LOGIFLOW_EXCHANGE);
    }

    @Bean
    public Binding createdBinding(Queue notificationQueue, TopicExchange logiflowExchange) {
        return BindingBuilder.bind(notificationQueue).to(logiflowExchange).with("pedido.creado");
    }

    @Bean
    public Binding assignedBinding(Queue notificationQueue, TopicExchange logiflowExchange) {
        return BindingBuilder.bind(notificationQueue).to(logiflowExchange).with("envio.asignado");
    }

    @Bean
    public Binding deliveredBinding(Queue notificationQueue, TopicExchange logiflowExchange) {
        return BindingBuilder.bind(notificationQueue).to(logiflowExchange).with("pedido.entregado");
    }

    @Bean
    public Binding cancelledBinding(Queue notificationQueue, TopicExchange logiflowExchange) {
        return BindingBuilder.bind(notificationQueue).to(logiflowExchange).with("pedido.cancelado");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
