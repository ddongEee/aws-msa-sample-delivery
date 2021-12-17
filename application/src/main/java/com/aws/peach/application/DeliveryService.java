package com.aws.peach.application;

import com.aws.peach.domain.delivery.*;
import com.aws.peach.domain.delivery.exception.DeliveryAlreadyExistsException;
import com.aws.peach.domain.delivery.exception.DeliveryNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Consumer;

@Component
public class DeliveryService {

    private final DeliveryRepository repository;

    public DeliveryService(DeliveryRepository repository) {
        this.repository = repository;
    }

    public DeliveryId createDeliveryOrder(Order order) {
        Optional<Delivery> existingDelivery = repository.findByOrderNo(order.getOrderNo());
        if (existingDelivery.isPresent()) {
            throw new DeliveryAlreadyExistsException(existingDelivery.get().getId());
        }
        Delivery delivery = Order.newDelivery(order);
        delivery = repository.save(delivery);
        return delivery.getId();
    }

    public void retrieveShipping() {
        // - shipment id 로 배송 이력을 조회할 수 있다
        // - order id 로 배송 이력을 조회할 수 있다
        // - 시간순으로 배송 이력을 정렬할 수 있다
        // - 상태 기준으로 배송 이력을 조회할 수 있다
    }

    public DeliveryId prepare(final DeliveryId deliveryId) {
        // TODO: DB 저장 및 메세지 발행
        return updateDeliveryStatus(deliveryId, Delivery::prepare);
    }

    public DeliveryId pack(final DeliveryId deliveryId) {
        return updateDeliveryStatus(deliveryId, Delivery::pack);
    }

    public DeliveryId ship(final DeliveryId deliveryId) {
        return updateDeliveryStatus(deliveryId, Delivery::ship);
    }

    private DeliveryId updateDeliveryStatus(final DeliveryId deliveryId, final Consumer<Delivery> updater) {
        Optional<Delivery> delivery = repository.findById(deliveryId);
        return delivery.map(delivery1 -> {
                    updater.accept(delivery1);
                    return delivery1.getId();
                }).orElseThrow(() -> new DeliveryNotFoundException(deliveryId));
    }
}
