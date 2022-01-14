package com.aws.peach.interfaces.api.model;

import com.aws.peach.domain.delivery.Delivery;
import com.aws.peach.interfaces.support.DtoUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public abstract class DeliveryResponse {

    @Builder
    @Getter
    protected static class Order {
        private final String orderNo;
        private final String openedAt;
        private final String ordererId;
        private final String ordererName;

        public static Order of(com.aws.peach.domain.delivery.Order o) {
            return Order.builder()
                    .orderNo(o.getNo().getValue())
                    .openedAt(DtoUtil.formatTimestamp(o.getOpenedAt()))
                    .ordererId(o.getOrderer().getId())
                    .ordererName(o.getOrderer().getName())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    protected static class DeliveryProduct {
        private final String name;
        private final int quantity;

        public static DeliveryProduct of(Delivery.DeliveryItem o) {
            return new DeliveryProduct(o.getName(), o.getQuantity());
        }
    }
}
