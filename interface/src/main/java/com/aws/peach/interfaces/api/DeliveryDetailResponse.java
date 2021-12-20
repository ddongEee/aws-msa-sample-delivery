package com.aws.peach.interfaces.api;

import com.aws.peach.domain.delivery.Delivery;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DeliveryDetailResponse {
    private String deliveryId;
    private String orderNo;
    private Sender sender;
    private Receiver receiver;
    private String status;

    public static DeliveryDetailResponse of(Delivery delivery) {
        Sender sender = Sender.builder()
                .id(delivery.getSender().getId())
                .name(delivery.getSender().getName())
                .build();
        Receiver receiver = Receiver.builder()
                .name(delivery.getReceiver().getName())
                .city(delivery.getReceiver().getCity())
                .zipCode(delivery.getReceiver().getZipCode())
                .country(delivery.getReceiver().getCountry())
                .telephone(delivery.getReceiver().getTelephone())
                .build();
        return DeliveryDetailResponse.builder()
                .deliveryId(delivery.getId().value)
                .orderNo(delivery.getOrderNo().value)
                .sender(sender)
                .receiver(receiver)
                .status(delivery.getStatus().getType().name())
                .build();
    }

    @Builder
    @Getter
    public static class Sender {
        private final String id;
        private final String name;
    }

    @Builder
    @Getter
    public static class Receiver {
        private final String name;
        private final String city;
        private final String zipCode;
        private final String country;
        private final String telephone;
    }
}
