package com.aws.peach.domain.delivery;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@ToString
@Builder
@Getter
public class DeliveryChangeEvent {
    private final String deliveryId;
    private final String orderNo;
    private final Address sendingAddress;
    private final Address shippingAddress;
    private final String status;
    private final String updatedAt;

    public static DeliveryChangeEvent of(Delivery delivery) {
        return DeliveryChangeEvent.builder()
                .deliveryId(delivery.getId().getValue())
                .orderNo(delivery.getOrderNo().getValue())
                .sendingAddress(delivery.getSender())
                .shippingAddress(delivery.getReceiver())
                .status(delivery.getStatus().getType().name())
                .updatedAt(formatTimestamp(delivery.getStatus().getTimestamp()))
                .build();
    }

    private static String formatTimestamp(Instant timestamp) {
        return DateTimeFormatter.ISO_INSTANT.format(timestamp);
    }
}
