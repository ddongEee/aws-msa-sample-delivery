package com.aws.peach.domain.delivery.exception;

import com.aws.peach.domain.delivery.DeliveryId;

public class DeliveryAlreadyExistsException extends RuntimeException {
    private final DeliveryId deliveryId;

    public DeliveryAlreadyExistsException(DeliveryId deliveryId) {
        this.deliveryId = deliveryId;
    }

    public DeliveryId getDeliveryId() {
        return deliveryId;
    }
}
