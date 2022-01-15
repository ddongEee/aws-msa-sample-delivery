package com.aws.peach.domain.delivery;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository {

    Optional<Delivery> findById(DeliveryId deliveryId);
    Optional<Delivery> findByOrderNo(OrderNo orderNo);
    Delivery save(Delivery delivery);
    List<Delivery> findAll(int pageNo, int pageSize);
    List<Delivery> findAllByStatus(DeliveryStatus.Type type, int pageNo, int pageSize);
}
