package com.aws.peach.domain.delivery;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class OrderNo {
    public final String value;

    public OrderNo(String value) {
        this.value = value;
    }
}
