package com.aws.peach.domain.delivery;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "value")
public class DeliveryId {
    public final String value;

    public DeliveryId(String value) {
        this.value = value;
    }
}
