package ec.edu.espe.msfacturacion.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String BILLING_QUEUE = "logiflow.billing.queue";
    public static final String LOGIFLOW_EXCHANGE = "logiflow.exchange";

    @Bean
    public Queue billingQueue() {
        return new Queue(BILLING_QUEUE, true);
    }

    @Bean
    public TopicExchange logiflowExchange() {
        return new TopicExchange(LOGIFLOW_EXCHANGE);
    }

    @Bean
    public Binding billingBinding(Queue billingQueue, TopicExchange logiflowExchange) {
        return BindingBuilder.bind(billingQueue).to(logiflowExchange).with("pedido.entregado");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
