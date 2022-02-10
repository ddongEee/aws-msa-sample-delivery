package com.aws.peach.domain.delivery;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(of = "value")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class OrderNo {
    private String value;

    public OrderNo(String value) {
        this.value = value;
    }
}
