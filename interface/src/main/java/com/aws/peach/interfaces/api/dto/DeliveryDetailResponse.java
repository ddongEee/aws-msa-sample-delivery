package com.aws.peach.interfaces.api.dto;

import com.aws.peach.domain.delivery.Address;
import com.aws.peach.domain.delivery.Delivery;
import com.aws.peach.interfaces.support.DtoUtil;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DeliveryDetailResponse {
    private final String deliveryId;
    private final OrderDto order;
    private final List<DeliveryItemDto> items;
    private final AddressDto sendingAddress;
    private final AddressDto shippingAddress;
    private final String status;
    private final String updatedAt;

    public static DeliveryDetailResponse of(Delivery delivery) {
        OrderDto order = OrderDto.of(delivery.getOrder());
        List<DeliveryItemDto> items = delivery.getItems().getMappedList(DeliveryItemDto::of);
        AddressDto sender = AddressDto.of(delivery.getSender());
        AddressDto receiver = AddressDto.of(delivery.getReceiver());
        return DeliveryDetailResponse.builder()
                .deliveryId(delivery.getId().getValue())
                .order(order)
                .items(items)
                .sendingAddress(sender)
                .shippingAddress(receiver)
                .status(delivery.getStatus().getType().name())
                .updatedAt(DtoUtil.formatTimestamp(delivery.getStatus().getTimestamp()))
                .build();
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
