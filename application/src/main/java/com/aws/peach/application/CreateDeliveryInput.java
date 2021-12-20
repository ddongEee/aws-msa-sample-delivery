package com.aws.peach.application;

import com.aws.peach.domain.delivery.*;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

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
                .telephone(o.receiver.telephone)
                .address1(o.receiver.address1)
                .address2(o.receiver.address2)
                .build();
        List<Delivery.DeliveryItem> items = o.order.products.stream()
                .map(OrderProductDto::newDeliveryItem)
                .collect(Collectors.toList());
        return new Delivery(order, sender, receiver, items);
    }

    @Builder
    public static class OrderDto {
        private final OrderNo id;
        private final Instant createdAt;
        private final String ordererId;
        private final String ordererName;
        private final List<OrderProductDto> products;
    }

    @Builder
    public static class OrderProductDto {
        private final String name;
        private final int qty;

        public static Delivery.DeliveryItem newDeliveryItem(OrderProductDto o) {
            return Delivery.DeliveryItem.builder()
                    .name(o.name)
                    .qty(o.qty)
                    .build();
        }
    }

    @Builder
    public static class Receiver {
        private final String name;
        private final String telephone;
        private final String city;
        private final String address1;
        private final String address2;
    }
}
