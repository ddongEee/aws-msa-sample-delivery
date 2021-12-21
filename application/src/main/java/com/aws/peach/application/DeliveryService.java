package com.aws.peach.application;

import com.aws.peach.domain.delivery.*;
import com.aws.peach.domain.delivery.exception.DeliveryAlreadyExistsException;
import com.aws.peach.domain.delivery.exception.DeliveryNotFoundException;
import com.aws.peach.domain.support.MessageProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Consumer;

@Service
public class DeliveryService {

    private final DeliveryRepository repository;
    private final MessageProducer<String, DeliveryChangeEvent> messageProducer;

    public DeliveryService(DeliveryRepository repository,
                           MessageProducer<String, DeliveryChangeEvent> messageProducer) {
        this.repository = repository;
        this.messageProducer = messageProducer;
    }

    public Delivery createDeliveryOrder(CreateDeliveryInput input) {
        Delivery delivery = CreateDeliveryInput.newDelivery(input, getSenderAddress());
        Optional<Delivery> existingDelivery = repository.findByOrderNo(delivery.getOrderNo()); // todo delegate check to the save method
        if (existingDelivery.isPresent()) {
            throw new DeliveryAlreadyExistsException(existingDelivery.get().getId());
        }
        return repository.save(delivery);
    }

    private Address getSenderAddress() {
        // TODO: 임시
        return Address.builder().build();
    }

    @Transactional(transactionManager = "transactionManager")
    public Delivery prepare(final DeliveryId deliveryId) {
        Delivery delivery = updateDeliveryStatus(deliveryId, Delivery::prepare);
        publishEvent(delivery);
        return delivery;
    }

    @Transactional(transactionManager = "transactionManager")
    public Delivery pack(final DeliveryId deliveryId) {
        Delivery delivery = updateDeliveryStatus(deliveryId, Delivery::pack);
        publishEvent(delivery);
        return delivery;
    }

    @Transactional(transactionManager = "transactionManager")
    public Delivery ship(final DeliveryId deliveryId) {
        Delivery delivery = updateDeliveryStatus(deliveryId, Delivery::ship);
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
    }
}
