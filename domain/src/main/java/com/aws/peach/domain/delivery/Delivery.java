package com.aws.peach.domain.delivery;

import com.aws.peach.domain.delivery.exception.DeliveryPrepareException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class Delivery {
    private DeliveryId id;
    private OrderNo orderNo; // monolith 서비스에서 생성한 orderNo
    private Sender sender;
    private Receiver receiver;
    private DeliveryStatus status;

    public Delivery(OrderNo orderNo, Sender sender, Receiver receiver) {
        this.orderNo = orderNo;
        this.sender = sender;
        this.receiver = receiver;
        this.status = DeliveryStatus.ORDER_RECEIVED;
    }

    public void prepare() {
        if (this.status != DeliveryStatus.ORDER_RECEIVED) {
            throw new DeliveryPrepareException(this.status);
        }
        this.status = DeliveryStatus.PREPARING;
    }

    public void pack() {
        if (this.status != DeliveryStatus.PREPARING) {
            throw new DeliveryPrepareException(this.status);
        }
        this.status = DeliveryStatus.PACKAGING;
    }

    public void ship() {
        if (this.status != DeliveryStatus.PACKAGING) {
            throw new DeliveryPrepareException(this.status);
        }
        this.status = DeliveryStatus.SHIPPED;
    }

    public DeliveryId getId() {
        return id;
    }

    @Builder
    public static class Sender {
        private final String id;
        private final String name;
    }

    @Builder
    public static class Receiver {
        private final String name;
        private final String city;
        private final String zipCode;
        private final String country;
        private final String telephone;
    }
}
