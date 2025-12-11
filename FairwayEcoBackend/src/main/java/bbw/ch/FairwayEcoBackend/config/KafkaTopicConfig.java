package bbw.ch.FairwayEcoBackend.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Kafka topic configuration.
 */
@Configuration
public class KafkaTopicConfig {

  @Bean
  public NewTopic orderCreatedTopic() {
    return TopicBuilder.name("order-created")
        .partitions(3)
        .replicas(1)
        .build();
  }

  @Bean
  public NewTopic orderStatusChangedTopic() {
    return TopicBuilder.name("order-status-changed")
        .partitions(3)
        .replicas(1)
        .build();
  }

  @Bean
  public NewTopic orderCancelledTopic() {
    return TopicBuilder.name("order-cancelled")
        .partitions(3)
        .replicas(1)
        .build();
  }

  @Bean
  public NewTopic stockUpdatedTopic() {
    return TopicBuilder.name("stock-updated")
        .partitions(3)
        .replicas(1)
        .build();
  }
}
