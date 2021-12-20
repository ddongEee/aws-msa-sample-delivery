package com.aws.peach.application;

import com.aws.peach.domain.delivery.Delivery;
import com.aws.peach.domain.delivery.Order;
import com.aws.peach.domain.delivery.OrderNo;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
public class CreateDeliveryInput {
    private OrderDto order;
    private Receiver receiver;

    public static Delivery newDelivery(CreateDeliveryInput o) {
        Order.Orderer orderer = Order.Orderer.builder()
                .id(o.order.ordererId)
                .name(o.order.ordererName)
                .build();
        Order order = Order.builder()
                .no(o.order.id)
                .openedAt(o.order.createdAt)
                .orderer(orderer)
                .build();
        Delivery.Sender sender = Delivery.Sender.builder() // todo not here
                .id(o.order.ordererId)
                .name(o.order.ordererName)
                .build();
        Delivery.Receiver receiver = Delivery.Receiver.builder()
                .name(o.receiver.name)
                .city(o.receiver.city)
                .zipCode(o.receiver.zipCode)
                .country(o.receiver.country)
                .telephone(o.receiver.telephone)
                .build();
        return new Delivery(order, sender, receiver);
    }

    @Builder
    public static class OrderDto {
        private final OrderNo id;
        private final Instant createdAt;
        private final String ordererId;
        private final String ordererName;
        // TODO order products
    }

    @Builder
    public static class Receiver {
        private String name;
        private String telephone;
        private String city;
        private String zipCode;
        private String country;
    }
}
