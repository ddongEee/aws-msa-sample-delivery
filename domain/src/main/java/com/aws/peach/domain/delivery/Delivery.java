package com.aws.peach.domain.delivery;

import com.aws.peach.domain.delivery.exception.DeliveryStateException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
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
    private final DeliveryItems items;

    public Delivery(Order order, Sender sender, Receiver receiver, List<DeliveryItem> items) {
        this.order = order; // todo validate (not null)
        this.sender = sender;
        this.receiver = receiver;
        this.status = new DeliveryStatus(DeliveryStatus.Type.ORDER_RECEIVED);
        this.items = new DeliveryItems(items); // TODO check not null
    }

    public void prepare() {
        if (this.status.canBePrepared()) {
            this.status = new DeliveryStatus(DeliveryStatus.Type.PREPARING);
        } else {
            throw new DeliveryStateException(this.id);
        }
    }

    public void pack() {
        if (this.status.canBePackaged()) {
            this.status = new DeliveryStatus(DeliveryStatus.Type.PACKAGING);
        } else{
            throw new DeliveryStateException(this.id);
        }
    }

    public void ship() {
        if (this.status.canBeShipped()) {
            this.status = new DeliveryStatus(DeliveryStatus.Type.SHIPPED);
        } else {
            throw new DeliveryStateException(this.id);
        }
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

    @Getter
    @Builder
    public static class DeliveryItem {
        private final String name;
        private final int qty;
    }

    @Getter
    private static class DeliveryItems {
        private final List<DeliveryItem> items;

        public DeliveryItems() {
            this(new ArrayList<>());
        }

        public DeliveryItems(List<DeliveryItem> items) {
            this.items = new ArrayList<>(items);
        }
    }
}
