package com.aws.peach.application;

import com.aws.peach.domain.delivery.*;
import com.aws.peach.domain.delivery.exception.DeliveryAlreadyExistsException;
import com.aws.peach.domain.delivery.exception.DeliveryNotFoundException;
import com.aws.peach.domain.support.MessageProducer;
import com.aws.peach.domain.test.TestMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Consumer;

@Service
@Transactional(transactionManager = "transactionManager")
public class DeliveryService {

    private final DeliveryRepository repository;
    private final MessageProducer<String, DeliveryChangeEvent> messageProducer;
    private final MessageProducer<String, TestMessage> testMessageProducer;

    public DeliveryService(final DeliveryRepository repository,
                           final MessageProducer<String, DeliveryChangeEvent> messageProducer,
                           final MessageProducer<String, TestMessage> testMessageProducer) {
        this.repository = repository;
        this.messageProducer = messageProducer;
        this.testMessageProducer = testMessageProducer;
    }

    public Delivery createDeliveryOrder(CreateDeliveryInput input) {
        Delivery delivery = CreateDeliveryInput.newDelivery(input, getSenderAddress());
        Optional<Delivery> existingDelivery = repository.findByOrderNo(delivery.getOrderNo());
        if (existingDelivery.isPresent()) {
            throw new DeliveryAlreadyExistsException(existingDelivery.get().getId());
        }
        Delivery deliveryResult = repository.save(delivery);
        testMessageProducer.send(null, new TestMessage("test", "test"));
        return deliveryResult;
    }

    private Address getSenderAddress() {// TODO: another service
        return Address.builder()
                .name("Good Farmer")
                .telephone("010-1111-2222")
                .city("Blue Mountain")
                .address1("Pine Valley 123")
                .build();
    }

    public Delivery prepare(final DeliveryId deliveryId) {
        Delivery delivery = updateDeliveryStatus(deliveryId, Delivery::prepare);
        publishEvent(delivery);
        return delivery;
    }

    public Delivery pack(final DeliveryId deliveryId) {
        Delivery delivery = updateDeliveryStatus(deliveryId, Delivery::pack);
        publishEvent(delivery);
        return delivery;
    }

    public Delivery ship(final DeliveryId deliveryId) {
        Delivery delivery = updateDeliveryStatus(deliveryId, Delivery::ship);
        publishEvent(delivery);
        return delivery;
    }

    public Delivery complete(final DeliveryId deliveryId) {
        Delivery delivery = updateDeliveryStatus(deliveryId, Delivery::complete);
        publishEvent(delivery);
        return delivery;
    }

    private Delivery updateDeliveryStatus(final DeliveryId deliveryId, final Consumer<Delivery> updater) {
        Optional<Delivery> delivery = repository.findById(deliveryId);
        delivery.ifPresent(delivery1 -> {
            updater.accept(delivery1);
            repository.save(delivery1);
        });
        return delivery.orElseThrow(() -> new DeliveryNotFoundException(deliveryId));
    }

    private void publishEvent(Delivery delivery) {
        DeliveryChangeEvent event = DeliveryChangeEvent.of(delivery);
        messageProducer.send(event.getDeliveryId(), event);
        testMessageProducer.send(null, new TestMessage("test", "test"));
    }
}
