package com.aws.peach.infrastructure.configuration;

import com.aws.peach.domain.delivery.DeliveryChangeMessage;
import com.aws.peach.domain.order.OrderStateChangeMessage;
import com.aws.peach.domain.support.MessageConsumer;
import com.aws.peach.domain.support.MessageProducer;
import com.aws.peach.domain.test.TestMessage;
import com.aws.peach.infrastructure.kafka.KafkaInfras;
import com.aws.peach.infrastructure.kafka.KafkaMessageListenerContainerFactory;
import com.aws.peach.infrastructure.kafka.KafkaMessageProducerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

@Configuration
@ComponentScan(basePackageClasses = {KafkaInfras.class})
public class KafkaMessageConfiguration {
    private final KafkaMessageListenerContainerFactory listenerContainerFactory;
    private final KafkaMessageProducerFactory producerFactory;

    public KafkaMessageConfiguration(final KafkaMessageListenerContainerFactory listenerContainerFactory,
                                     final KafkaMessageProducerFactory producerFactory) {
        this.listenerContainerFactory = listenerContainerFactory;
        this.producerFactory = producerFactory;
    }

    @Bean
    public KafkaMessageListenerContainer<String, OrderStateChangeMessage> orderStateChangeMessageListenerContainer(
            @Value("${kafka.topic.order-state-change}") final String topic,
            final MessageConsumer<OrderStateChangeMessage> messageConsumer,
            final ConsumerFactory<String,OrderStateChangeMessage> consumerFactory,
            final KafkaTemplate<String, OrderStateChangeMessage> kafkaTemplate) {

        return this.listenerContainerFactory.create(topic, messageConsumer, consumerFactory, kafkaTemplate);
    }

    @Bean
    public KafkaMessageListenerContainer<String, TestMessage> testMessageListenerContainer(
            @Value("${kafka.topic.delivery-test-message}") final String topic,
            final MessageConsumer<TestMessage> messageConsumer,
            final ConsumerFactory<String,TestMessage> consumerFactory,
            final KafkaTemplate<String, TestMessage> kafkaTemplate) {

        return this.listenerContainerFactory.create(topic, messageConsumer, consumerFactory, kafkaTemplate);
    }

    @Bean
    public MessageProducer<String, DeliveryChangeMessage> deliveryChangeMessageProducer(final KafkaTemplate<String, DeliveryChangeMessage> kafkaTemplate,
                                                                                       @Value("${kafka.topic.delivery-change}") final String topic) {
        return this.producerFactory.create(kafkaTemplate, topic);
    }

    @Bean
    public MessageProducer<String, TestMessage> testMessageProducer(final KafkaTemplate<String, TestMessage> kafkaTemplate,
                                                                    @Value("${kafka.topic.delivery-test-message}") final String topic) {
        return this.producerFactory.create(kafkaTemplate, topic);
    }
}
