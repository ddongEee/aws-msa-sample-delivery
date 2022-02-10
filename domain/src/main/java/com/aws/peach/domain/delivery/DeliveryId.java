package com.aws.peach.domain.delivery;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(of = "value")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class DeliveryId {
    private String value;

    public DeliveryId(String value) {
        this.value = value;
    }
}
