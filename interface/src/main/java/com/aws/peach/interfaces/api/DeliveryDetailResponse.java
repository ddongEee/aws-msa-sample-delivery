package com.aws.peach.interfaces.api;

import com.aws.peach.domain.delivery.Address;
import com.aws.peach.domain.delivery.Delivery;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Builder
@Getter
public class DeliveryDetailResponse {
    private final String deliveryId;
    private final String orderNo;
    private final AddressDto sendingAddress;
    private final AddressDto shippingAddress;
    private final String status;
    private final String updatedAt;

    public static DeliveryDetailResponse of(Delivery delivery) {
        AddressDto sender = AddressDto.of(delivery.getSender());
        AddressDto receiver = AddressDto.of(delivery.getReceiver());
        return DeliveryDetailResponse.builder()
                .deliveryId(delivery.getId().value)
                .orderNo(delivery.getOrderNo().value)
                .sendingAddress(sender)
                .shippingAddress(receiver)
                .status(delivery.getStatus().getType().name())
                .updatedAt(formatTimestamp(delivery.getStatus().getTimestamp()))
                .build();
    }

    private static String formatTimestamp(Instant timestamp) {
        return DateTimeFormatter.ISO_INSTANT.format(timestamp);
    }

    @Builder
    @Getter
    public static class AddressDto {
        private final String name;
        private final String city;
        private final String telephone;
        private final String address1;
        private final String address2;

        public static AddressDto of(Address o) {
            return AddressDto.builder()
                    .name(o.getName())
                    .city(o.getCity())
                    .telephone(o.getTelephone())
                    .address1(o.getAddress1())
                    .address2(o.getAddress2())
                    .build();
        }
    }
}
