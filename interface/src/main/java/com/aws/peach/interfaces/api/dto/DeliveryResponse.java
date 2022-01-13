package com.aws.peach.interfaces.api.dto;

import com.aws.peach.domain.delivery.Delivery;
import com.aws.peach.interfaces.support.DtoUtil;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DeliveryResponse {
    private final String deliveryId;
    private final OrderDto order;
    private final List<DeliveryItemDto> items;
    private final String status;
    private final String updatedAt;

    public static DeliveryResponse of(Delivery delivery) {
        OrderDto order = OrderDto.of(delivery.getOrder());
        List<DeliveryItemDto> items = delivery.getItems().getMappedList(DeliveryItemDto::of);
        return DeliveryResponse.builder()
                .deliveryId(delivery.getId().getValue())
                .order(order)
                .items(items)
                .status(delivery.getStatus().getType().name())
                .updatedAt(DtoUtil.formatTimestamp(delivery.getStatus().getTimestamp()))
                .build();
    }
}
