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
                .telephone(delivery.getReceiver().getTelephone())
                .address1(delivery.getReceiver().getAddress1())
                .address2(delivery.getReceiver().getAddress2())
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
        private final String telephone;
        private final String address1;
        private final String address2;
    }
}
