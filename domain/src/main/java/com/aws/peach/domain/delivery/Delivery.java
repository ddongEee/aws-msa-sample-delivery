package com.aws.peach.domain.delivery;

import com.aws.peach.domain.delivery.exception.DeliveryPrepareException;

public class Delivery { // root entity
//    private final String id;
    private final OrderNo orderNo; // monolith 서비스에서 생성한 orderNo
//    private final String sender;
//    private final String senderAddress;
//    private final String receiver;
//    private final String receiverAddress;
    private DeliveryStatus status;

    private Delivery(OrderNo orderNo) {
        this.orderNo = orderNo;
        this.status = DeliveryStatus.ORDER_RECEIVED;
    }

    public void prepare() {
        if (this.status != DeliveryStatus.ORDER_RECEIVED) {
            throw new DeliveryPrepareException(this.status);
        }
        this.status = DeliveryStatus.PREPARING;
    }

    public void ship() {
        if (this.status != DeliveryStatus.PACKAGING) {
            throw new DeliveryPrepareException(this.status);
        }
        this.status = DeliveryStatus.SHIPPED;
    }

    public static class Builder {
        private final OrderNo orderNo;
        private DeliveryStatus status;

        public Builder(OrderNo orderNo) {
            this.orderNo = orderNo;
        }

        public Builder status(DeliveryStatus status) {
            this.status = status;
            return this;
        }

        public Delivery build() {
            Delivery delivery = new Delivery(this.orderNo);
            if (this.status != null) {
                delivery.status = status;
            }
            return delivery;
        }
    }
}
