package com.aws.peach.infrastructure.dummy;

import com.aws.peach.domain.delivery.Delivery;
import com.aws.peach.domain.delivery.DeliveryRepository;
import com.aws.peach.domain.delivery.OrderNo;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class DeliveryDummyRepository implements DeliveryRepository {

    private final Map<OrderNo, Delivery> map;

    public DeliveryDummyRepository() {
        this.map = new ConcurrentHashMap<>();
    }

    DeliveryDummyRepository(Map<OrderNo, Delivery> map) {
        this.map = new ConcurrentHashMap<>(map);
    }

    @Override
    public Optional<Delivery> findByOrderNo(OrderNo orderNo) {
        return Optional.ofNullable(map.get(orderNo));
    }

    @Override
    public Delivery save(Delivery delivery) {
        delivery.assignNewId();
        map.put(delivery.getOrderNo(), delivery);
        return delivery;
    }
}
