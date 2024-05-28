package com.skillstorm.budgetservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
  
  @Value("${exchanges.fanout}")
  private String fanoutExchangeName;

  @Value("${queues.fanout.budgetService}")
  private String fanoutQueueBudgetService;

  @Value("${queues.fanout.bucketService}")
  private String fanoutQueueBucketService;

  // creating the exchange
  @Bean
  public Exchange fanoutExchange() {
    return new FanoutExchange(fanoutExchangeName);
  }

  // creating the first queue
  @Bean
  public Queue queueBudgetService() {
      return new Queue(fanoutQueueBudgetService);
  }

  // creating the second queue
  @Bean
  public Queue queueBucketService() {
      return new Queue(fanoutQueueBucketService);
  }

  // creating the binding between the first queue and the exchange
  @Bean
  public Binding bindingBudgetService(FanoutExchange fanoutExchange, Queue queueBudgetService) {
      return BindingBuilder.bind(queueBudgetService).to(fanoutExchange);
  }

  // creating the binding between the second queue and the exchange
  @Bean
  public Binding bindingBucketService(FanoutExchange fanoutExchange, Queue queueBucketService) {
      return BindingBuilder.bind(queueBucketService).to(fanoutExchange);
  }

  // creating the message converter
  @Bean
  public MessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
