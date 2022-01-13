package com.aws.peach.interfaces.api.dto;

import com.aws.peach.domain.delivery.Delivery;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryItemDto {
    private final String name;
    private final int quantity;

    public static DeliveryItemDto of(Delivery.DeliveryItem o) {
        return new DeliveryItemDto(o.getName(), o.getQuantity());
    }
}
