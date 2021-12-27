package com.aws.peach.infrastructure.configuration;

import com.aws.peach.domain.delivery.DeliveryChangeEvent;
import com.aws.peach.domain.support.MessageProducer;
import com.aws.peach.infrastructure.kafka.KafkaInfras;
import com.aws.peach.infrastructure.kafka.KafkaMessageProducerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan(basePackageClasses = {KafkaInfras.class})
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class MessageProducerConfiguration {
    private final KafkaMessageProducerFactory kafkaMessageProducerFactory;

    public MessageProducerConfiguration(final KafkaMessageProducerFactory kafkaMessageProducerFactory) {
        this.kafkaMessageProducerFactory = kafkaMessageProducerFactory;
    }

    @Bean
    public MessageProducer<String, DeliveryChangeEvent> deliveryChangeEventProducer(
            @Value("${spring.kafka.bootstrap-servers}") final String bootstrapServers,
            @Value("${kafka.topic.delivery-event}") final String topic) {
        return kafkaMessageProducerFactory.create(bootstrapServers, topic);
    }
}
