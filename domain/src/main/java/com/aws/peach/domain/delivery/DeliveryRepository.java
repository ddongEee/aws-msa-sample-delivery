package com.aws.peach.domain.delivery;

import java.util.Optional;

public interface DeliveryRepository {

    Optional<Delivery> findByOrderNo(OrderNo orderNo);
}
