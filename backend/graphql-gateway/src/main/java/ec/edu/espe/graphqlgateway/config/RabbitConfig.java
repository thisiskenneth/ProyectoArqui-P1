package ec.edu.espe.graphqlgateway.config;

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
    public static final String GATEWAY_TRACKING_QUEUE = "logiflow.gateway.tracking.queue";

    @Bean
    public Queue gatewayTrackingQueue() {
        return new Queue(GATEWAY_TRACKING_QUEUE, true);
    }

    @Bean
    public TopicExchange logiflowExchange() {
        return new TopicExchange(LOGIFLOW_EXCHANGE);
    }

    @Bean
    public Binding gatewayTrackingBinding(Queue gatewayTrackingQueue, TopicExchange logiflowExchange) {
        return BindingBuilder.bind(gatewayTrackingQueue).to(logiflowExchange).with("posicion.actualizada");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
