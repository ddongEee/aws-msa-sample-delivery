package com.aws.peach.domain.delivery.exception;

import com.aws.peach.domain.delivery.OrderNo;

public class DeliveryNotFoundException extends RuntimeException {
    private final OrderNo orderNo;

    public DeliveryNotFoundException(OrderNo orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo() {
        return orderNo.value;
    }
}
