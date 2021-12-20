package com.aws.peach.application;

import com.aws.peach.domain.delivery.*;
import com.aws.peach.domain.delivery.exception.DeliveryAlreadyExistsException;
import com.aws.peach.domain.delivery.exception.DeliveryNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Consumer;

@Service
public class DeliveryService {

    private final DeliveryRepository repository;

    public DeliveryService(DeliveryRepository repository) {
        this.repository = repository;
    }

    public Delivery createDeliveryOrder(CreateDeliveryInput input) {
        Delivery delivery = CreateDeliveryInput.newDelivery(input);
        Optional<Delivery> existingDelivery = repository.findByOrderNo(delivery.getOrderNo()); // todo delegate check to the save method
        if (existingDelivery.isPresent()) {
            throw new DeliveryAlreadyExistsException(existingDelivery.get().getId());
        }
        return repository.save(delivery);
    }

    public Delivery prepare(final DeliveryId deliveryId) {
        // TODO: DB 저장 및 메세지 발행
        return updateDeliveryStatus(deliveryId, Delivery::prepare);
    }

    public Delivery pack(final DeliveryId deliveryId) {
        return updateDeliveryStatus(deliveryId, Delivery::pack);
    }

    public Delivery ship(final DeliveryId deliveryId) {
        return updateDeliveryStatus(deliveryId, Delivery::ship);
    }

    private Delivery updateDeliveryStatus(final DeliveryId deliveryId, final Consumer<Delivery> updater) {
        Optional<Delivery> delivery = repository.findById(deliveryId);
        delivery.ifPresent(updater);
        return delivery.orElseThrow(() -> new DeliveryNotFoundException(deliveryId));
    }
}
