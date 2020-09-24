package me.vukas.benchmarking.benchmarks.rabbit;

import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.DirectRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

  public final static String queuej = "benchmarking.queue.json";
  public final static String queuep = "benchmarking.queue.proto";

  @Bean
  RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
    return new RabbitTemplate(connectionFactory);
  }

  @Bean
  Queue queuej() {
    Map<String, Object> args = new HashMap<>();
    args.put("x-max-length", 500);
    return new Queue(queuej, false, false, true, args);
  }

  @Bean
  Queue queuep() {
    Map<String, Object> args = new HashMap<>();
    args.put("x-max-length", 500);
    return new Queue(queuep, false, false, true, args);
  }

  @Bean
  public RabbitListenerContainerFactory<DirectMessageListenerContainer> prefetchFiveRabbitListenerContainerFactory(ConnectionFactory rabbitConnectionFactory) {
    DirectRabbitListenerContainerFactory factory = new DirectRabbitListenerContainerFactory();
    factory.setConnectionFactory(rabbitConnectionFactory);
    factory.setPrefetchCount(5);
    return factory;
  }

}
