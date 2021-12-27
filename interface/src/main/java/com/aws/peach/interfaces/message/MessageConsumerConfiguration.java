package com.aws.peach.interfaces.message;

import com.aws.peach.domain.support.MessageConsumer;
import com.aws.peach.infrastructure.kafka.KafkaMessageConsumerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

@Slf4j
@Configuration
@ComponentScan(basePackageClasses = {Messages.class})
public class MessageConsumerConfiguration {
    private final KafkaMessageConsumerFactory kafkaMessageConsumerFactory;
    private final String bootstrapServers;
    private final String groupId;

    public MessageConsumerConfiguration(KafkaMessageConsumerFactory kafkaMessageConsumerFactory,
                                        @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
                                        @Value("${spring.kafka.consumer.group-id}") String groupId) {
        this.kafkaMessageConsumerFactory = kafkaMessageConsumerFactory;
        this.bootstrapServers = bootstrapServers;
        this.groupId = groupId;
    }

    @Bean
    public KafkaMessageListenerContainer<String, OrderStateChangeMessage> orderStateChangeMessageListener(
            @Value("${kafka.topic.order-state-change}") final String topic,
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
