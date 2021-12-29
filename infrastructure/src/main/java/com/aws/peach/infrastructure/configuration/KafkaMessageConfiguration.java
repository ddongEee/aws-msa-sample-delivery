package com.aws.peach.infrastructure.configuration;

import com.aws.peach.domain.delivery.DeliveryChangeEvent;
import com.aws.peach.domain.order.OrderStateChangeMessage;
import com.aws.peach.domain.support.MessageConsumer;
import com.aws.peach.domain.support.MessageProducer;
import com.aws.peach.infrastructure.kafka.KafkaInfras;
import com.aws.peach.infrastructure.kafka.KafkaMessageConsumerFactory;
import com.aws.peach.infrastructure.kafka.KafkaMessageProducerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

@Slf4j
@Configuration
@ComponentScan(basePackageClasses = {KafkaInfras.class})
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class KafkaMessageConfiguration {
    private final KafkaMessageProducerFactory kafkaMessageProducerFactory;
    private final KafkaMessageConsumerFactory kafkaMessageConsumerFactory;

    public KafkaMessageConfiguration(final KafkaMessageProducerFactory kafkaMessageProducerFactory,
                                     final KafkaMessageConsumerFactory kafkaMessageConsumerFactory) {
        this.kafkaMessageProducerFactory = kafkaMessageProducerFactory;
        this.kafkaMessageConsumerFactory = kafkaMessageConsumerFactory;
    }

    @Bean
    public MessageProducer<String, DeliveryChangeEvent> deliveryChangeEventProducer(
            @Value("${spring.kafka.bootstrap-servers}") final String bootstrapServers,
            @Value("${kafka.topic.delivery-event}") final String topic) {
        return kafkaMessageProducerFactory.create(bootstrapServers, topic);
    }

    @Bean
    public KafkaMessageListenerContainer<String, OrderStateChangeMessage> orderStateChangeMessageListener(
            @Value("${spring.kafka.bootstrap-servers}") final String bootstrapServers,
            @Value("${kafka.topic.order-state-change}") final String topic,
            @Value("${spring.kafka.consumer.group-id}") final String groupId,
            final MessageConsumer<OrderStateChangeMessage> messageConsumer) {

        KafkaMessageConsumerFactory.ConsumerProperties<OrderStateChangeMessage> consumerProperties =
                KafkaMessageConsumerFactory.ConsumerProperties.<OrderStateChangeMessage>builder()
                        .serverUrl(bootstrapServers)
                        .topic(topic)
                        .groupId(groupId)
                        .messageType(OrderStateChangeMessage.class)
                        .messageConsumer(messageConsumer)
                        .build();
        return kafkaMessageConsumerFactory.create(consumerProperties);
    }
}
