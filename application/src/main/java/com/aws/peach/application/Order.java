package com.aws.peach.application;

import com.aws.peach.domain.delivery.Delivery;
import com.aws.peach.domain.delivery.OrderNo;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Order {
    private OrderNo orderNo;
    private Orderer orderer;
    private Receiver receiver;

    public static Delivery newDelivery(Order o) {
        Delivery.Sender sender = Delivery.Sender.builder()
                .id(o.orderer.id)
                .name(o.orderer.name)
                .build();
        Delivery.Receiver receiver = Delivery.Receiver.builder()
                .name(o.receiver.name)
                .city(o.receiver.city)
                .zipCode(o.receiver.zipCode)
                .country(o.receiver.country)
                .telephone(o.receiver.telephone)
                .build();
        return new Delivery(o.orderNo, sender, receiver);
    }

    @Builder
    public static class Orderer {
        private String id;
        private String name;
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
