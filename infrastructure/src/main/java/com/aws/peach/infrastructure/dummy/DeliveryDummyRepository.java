package com.aws.peach.infrastructure.dummy;

import com.aws.peach.domain.delivery.Delivery;
import com.aws.peach.domain.delivery.DeliveryId;
import com.aws.peach.domain.delivery.DeliveryRepository;
import com.aws.peach.domain.delivery.OrderNo;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

//@Repository
public class DeliveryDummyRepository implements DeliveryRepository {

    private final Map<OrderNo, Delivery> orderNo2Delivery;
    private final Map<DeliveryId, Delivery> deliveryId2Delivery;

    public DeliveryDummyRepository() {
        this.orderNo2Delivery = new ConcurrentHashMap<>();
        this.deliveryId2Delivery = new ConcurrentHashMap<>();
    }

    DeliveryDummyRepository(Collection<Delivery> initData) {
        this.orderNo2Delivery = initData.stream()
                .collect(Collectors.toMap(Delivery::getOrderNo, Function.identity()));
        this.deliveryId2Delivery = initData.stream()
                .collect(Collectors.toMap(Delivery::getId, Function.identity()));
    }

    @Override
    public Optional<Delivery> findById(DeliveryId deliveryId) {
        return Optional.ofNullable(this.deliveryId2Delivery.get(deliveryId));
    }

    @Override
    public Optional<Delivery> findByOrderNo(OrderNo orderNo) {
        return Optional.ofNullable(orderNo2Delivery.get(orderNo));
    }

    @Override
    public Delivery save(Delivery delivery) {
        delivery.assignNewId();
        orderNo2Delivery.put(delivery.getOrderNo(), delivery);
        deliveryId2Delivery.put(delivery.getId(), delivery);
        return delivery;
    }
}
