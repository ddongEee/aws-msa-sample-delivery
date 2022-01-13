package com.aws.peach.interfaces.api.dto;

import com.aws.peach.domain.delivery.Order;
import com.aws.peach.interfaces.support.DtoUtil;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderDto {
    private final String orderNo;
    private final String openedAt;
    private final String ordererId;
    private final String ordererName;

    public static OrderDto of(Order o) {
        return OrderDto.builder()
                .orderNo(o.getNo().getValue())
                .openedAt(DtoUtil.formatTimestamp(o.getOpenedAt()))
                .ordererId(o.getOrderer().getId())
                .ordererName(o.getOrderer().getName())
                .build();
    }
}
