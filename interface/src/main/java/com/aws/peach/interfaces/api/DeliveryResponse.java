package com.aws.peach.interfaces.api;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DeliveryResponse {
    private String deliveryId;
}
