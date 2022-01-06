package com.aws.peach.infrastructure.kafka;

import com.aws.peach.domain.support.MessageConsumer;
import com.aws.peach.domain.support.exception.InvalidMessageException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.*;
import org.springframework.stereotype.Component;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Component
public class KafkaMessageListenerContainerFactory {

    public <M> KafkaMessageListenerContainer<String,M> create(final String topic,
                                                              final MessageConsumer<M> messageConsumer,
                                                              final ConsumerFactory<String, M> consumerFactory,
                                                              final KafkaTemplate<String, M> kafkaTemplate) {
        ContainerProperties containerProps = containerProps(topic, messageConsumer);
        KafkaMessageListenerContainer<String, M> container = new KafkaMessageListenerContainer<>(consumerFactory, containerProps);
        container.setCommonErrorHandler(errorHandler(kafkaTemplate));
        return container;
    }

    private <M> ContainerProperties containerProps(final String topic, final MessageConsumer<M> messageConsumer) {
        ContainerProperties containerProps = new ContainerProperties(topic);
        containerProps.setMessageListener((MessageListener<String,M>) data -> messageConsumer.consume(data.value()));
        // TODO: https://docs.spring.io/spring-kafka/reference/html/#default-eh
        containerProps.setAckMode(ContainerProperties.AckMode.RECORD);
        return containerProps;
    }

    private <M> CommonErrorHandler errorHandler(final KafkaTemplate<String, M> kafkaTemplate) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate, (record, exception) -> {
            if (exception instanceof InvalidMessageException) {
                return new TopicPartition(record.topic() + ".invalid.DLT", record.partition());
            }
            return new TopicPartition(record.topic() + ".DLT", record.partition());
        });
        return new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 2L));
    }
}
