package com.aws.peach.domain.delivery;

import com.aws.peach.domain.delivery.exception.DeliveryStateException;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Delivery {
    private DeliveryId id;
    private Order order;
    private Address sender;
    private Address receiver;
    private DeliveryStatus status;
    private DeliveryItems items;

    public Delivery(Order order, Address sender, Address receiver, List<DeliveryItem> items) {
        this.order = order; // todo validate (not null)
        this.sender = sender; // todo validate (not null)
        this.receiver = receiver; // todo validate (not null)
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
    public static class DeliveryItem {
        private final String name;
        private final int qty; // TODO quantity
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
