package com.skillstorm.budgetservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${eureka.instance.hostname}")
    private String host;

    // Exchanges:
    @Value("${exchanges.direct}")
    private String directExchange;

    // Set up credentials and connect to RabbitMQ:
    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    // Configure the RabbitTemplate:
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(messageConverter());
        rabbitTemplate.setReplyTimeout(6000);
        return rabbitTemplate;
    }

    // Create the exchange:
    @Bean
    public Exchange directExchange() {
        return new DirectExchange(directExchange);
    }

    // Create the queues:
    @Bean
    public Queue transactionRequestQueue() {
        return new Queue("budget-request");
    }

    @Bean
    public Queue transactionResponseQueue() {
        return new Queue("budget-response");
    }

    // Bind the queues to the exchange:
    @Bean
    public Binding transactionRequestBinding(Queue transactionRequestQueue, Exchange directExchange) {
        return BindingBuilder.bind(transactionRequestQueue)
                .to(directExchange)
                .with("budget-request")
                .noargs();
    }

    @Bean
    public Binding transactionResponseBinding(Queue transactionResponseQueue, Exchange directExchange) {
        return BindingBuilder.bind(transactionResponseQueue)
                .to(directExchange)
                .with("budget-response")
                .noargs();
    }

    // Serialize Java objects to JSON:
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
