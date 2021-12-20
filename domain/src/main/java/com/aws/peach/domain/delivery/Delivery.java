package com.aws.peach.domain.delivery;

import com.aws.peach.domain.delivery.exception.DeliveryStateException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class Delivery {
    private DeliveryId id;
    private final Order order;
    private Sender sender;
    private Receiver receiver;
    private DeliveryStatus status;

    public Delivery(Order order, Sender sender, Receiver receiver) {
        this.order = order; // todo validate (not null)
        this.sender = sender;
        this.receiver = receiver;
        this.status = DeliveryStatus.ORDER_RECEIVED;
    }

    public void prepare() {
        if (this.status != DeliveryStatus.ORDER_RECEIVED) {
            throw new DeliveryStateException(this.id);
        }
        this.status = DeliveryStatus.PREPARING;
    }

    public void pack() {
        if (this.status != DeliveryStatus.PREPARING) {
            throw new DeliveryStateException(this.id);
        }
        this.status = DeliveryStatus.PACKAGING;
    }

    public void ship() {
        if (this.status != DeliveryStatus.PACKAGING) {
            throw new DeliveryStateException(this.id);
        }
        this.status = DeliveryStatus.SHIPPED;
    }

    public DeliveryId getId() {
        return id;
    }

    public void assignNewId() {
        UUID uuid = UUID.randomUUID();
        this.id = new DeliveryId(uuid.toString());
    }

    public OrderNo getOrderNo() {
        return this.order.getNo();
    }

    @Getter
    @Builder
    public static class Sender {
        private final String id;
        private final String name;
    }

    @Getter
    @Builder
    public static class Receiver {
        private final String name;
        private final String city;
        private final String zipCode;
        private final String country;
        private final String telephone;
    }
}
