package com.aws.peach.domain.delivery.exception;

import com.aws.peach.domain.delivery.DeliveryStatus;

public class DeliveryPrepareException extends RuntimeException {
    private final DeliveryStatus currentStatus;

    public DeliveryPrepareException(DeliveryStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public DeliveryStatus getCurrentStatus() {
        return currentStatus;
    }
}
