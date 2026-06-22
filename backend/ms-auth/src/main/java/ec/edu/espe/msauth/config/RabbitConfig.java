package ec.edu.espe.msauth.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String LOGIFLOW_EXCHANGE = "logiflow.exchange";
    public static final String CLIENTE_CREATED_QUEUE = "logiflow.auth.cliente.created.queue";
    public static final String CLIENTE_CREATED_ROUTING_KEY = "cliente.creado";

    @Bean
    public Queue clienteCreatedQueue() {
        return new Queue(CLIENTE_CREATED_QUEUE, true);
    }

    @Bean
    public TopicExchange logiflowExchange() {
        return new TopicExchange(LOGIFLOW_EXCHANGE);
    }

    @Bean
    public Binding clienteCreatedBinding(Queue clienteCreatedQueue, TopicExchange logiflowExchange) {
        return BindingBuilder.bind(clienteCreatedQueue).to(logiflowExchange).with(CLIENTE_CREATED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        // Deserializa el JSON al tipo del parametro del @RabbitListener (ClienteCreatedEvent local),
        // ignorando el header __TypeId__ que trae el nombre de clase del productor (ms-clientes).
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}
