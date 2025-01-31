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

    @Value("{rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey.user}")
    private String userRegisteredRoutingKey;

    @Value("${rabbitmq.queue.user}")
    private String userQueue;

    @Value("${rabbitmq.routingkey.order}")
    private String orderCreatedRoutingKey;

    @Value("${rabbitmq.queue.order}")
    private String orderQueue;

    @Value("${rabbitmq.queue.stock.notification}")
    private String stockNotificationQueue;

    @Value("${rabbitmq.routing.stock.reduced}")
    private String stockReducedRoutingKey;

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue userQueue() {
        return new Queue(userQueue);
    }

    @Bean
    public Queue orderQueue() {
        return new Queue(orderQueue);
    }

    @Bean
    public Queue stockNotificationQueue() {
        return new Queue(stockNotificationQueue, true);
    }

    @Bean
    public Binding bindingStockNotification(Queue stockNotificationQueue, TopicExchange stockExchange) {
        return BindingBuilder.bind(stockNotificationQueue).to(stockExchange).with(stockReducedRoutingKey);
    }

    @Bean
    public Binding bindingUserQueue(Queue userQueue, TopicExchange topicExchange) {
        return BindingBuilder
                .bind(userQueue)
                .to(topicExchange)
                .with(userRegisteredRoutingKey);
    }

    @Bean
    public Binding bindingOrderQueue(Queue orderQueue, TopicExchange topicExchange) {
        return BindingBuilder
                .bind(orderQueue)
                .to(topicExchange)
                .with(orderCreatedRoutingKey);
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
