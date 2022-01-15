package com.aws.peach.interfaces.api.model;

import com.aws.peach.domain.delivery.Delivery;
import com.aws.peach.interfaces.support.DtoUtil;
import lombok.*;

public abstract class DeliveryResponse {

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    protected static class Order {
        private String orderNo;
        private String openedAt;
        private String ordererId;
        private String ordererName;

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
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    protected static class DeliveryProduct {
        private String name;
        private int quantity;

        public static DeliveryProduct of(Delivery.DeliveryItem o) {
            return new DeliveryProduct(o.getName(), o.getQuantity());
        }
    }
}
