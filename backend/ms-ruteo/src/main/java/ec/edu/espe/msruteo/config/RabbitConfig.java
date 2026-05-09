package ec.edu.espe.msruteo.config;

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
    public static final String ORDER_CREATED_QUEUE = "logiflow.ruteo.order.created.queue";

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(ORDER_CREATED_QUEUE, true);
    }

    @Bean
    public TopicExchange logiflowExchange() {
        return new TopicExchange(LOGIFLOW_EXCHANGE);
    }

    @Bean
    public Binding orderCreatedBinding(Queue orderCreatedQueue, TopicExchange logiflowExchange) {
        return BindingBuilder.bind(orderCreatedQueue).to(logiflowExchange).with("pedido.creado");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
