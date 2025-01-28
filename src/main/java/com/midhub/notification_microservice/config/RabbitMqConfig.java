package com.midhub.notification_microservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.user.registered.routing-key}")
    private String userRegisteredRoutingKey;

    @Value("${rabbitmq.pdf.generated.routing-key}")
    private String pdfGeneratedRoutingKey;

    @Value("${rabbitmq.queues.user}")
    private String userQueue;

    @Value("${rabbitmq.queues.pdf}")
    private String pdfQueue;

    // Declarar la cola para usuarios registrados
    @Bean
    public Queue userRegisteredQueue() {
        return new Queue(userQueue, true);
    }

    // Declarar la cola para generación de PDFs de órdenes
    @Bean
    public Queue orderPdfQueue() {
        return new Queue(pdfQueue, true);
    }

    // Declarar un exchange común
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    // Binding para la cola de usuarios registrados
    @Bean
    public Binding userRegisteredBinding(Queue userRegisteredQueue, TopicExchange exchange) {
        return BindingBuilder.bind(userRegisteredQueue).to(exchange).with(userRegisteredRoutingKey);
    }

    // Binding para la cola de generación de PDFs
    @Bean
    public Binding orderPdfBinding(Queue orderPdfQueue, TopicExchange exchange) {
        return BindingBuilder.bind(orderPdfQueue).to(exchange).with(pdfGeneratedRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
