package ec.edu.espe.msseguimiento.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String TRACKING_QUEUE = "logiflow.tracking.queue";
    public static final String LOGIFLOW_EXCHANGE = "logiflow.exchange";

    @Bean
    public Queue trackingQueue() {
        return new Queue(TRACKING_QUEUE, true);
    }

    @Bean
    public TopicExchange logiflowExchange() {
        return new TopicExchange(LOGIFLOW_EXCHANGE);
    }

    @Bean
    public Binding trackingBinding(Queue trackingQueue, TopicExchange logiflowExchange) {
        return BindingBuilder.bind(trackingQueue).to(logiflowExchange).with("posicion.actualizada");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
