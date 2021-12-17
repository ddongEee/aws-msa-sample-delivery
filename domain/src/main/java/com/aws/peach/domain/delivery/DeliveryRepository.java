package com.aws.peach.domain.delivery;

import java.util.Optional;

public interface DeliveryRepository {

    Optional<Delivery> findById(DeliveryId deliveryId);
    Optional<Delivery> findByOrderNo(OrderNo orderNo);
    Delivery save(Delivery delivery);
}
