package com.aws.peach.interfaces.api;

import com.aws.peach.application.Order;
import com.aws.peach.domain.delivery.OrderNo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@ToString
@Getter
public class ReceiveDeliveryOrderRequest {
    @NotNull
    private final String orderNo;
    @NotNull
    private final Orderer orderer;
    @NotEmpty
    private final List<OrderLine> orderLines;
    private final String orderState;
    @NotNull
    private final String orderDate;
    @NotNull
    private final ShippingInfo shippingInformation;

    public static Order newOrder(ReceiveDeliveryOrderRequest req) {
        OrderNo orderNo = new OrderNo(req.orderNo);
        Order.Orderer orderer = Order.Orderer.builder()
                    .id(req.orderer.memberId)
                    .name(req.orderer.name)
                    .build();
        Order.Receiver receiver = Order.Receiver.builder()
                    .name(req.shippingInformation.receiver)
                    .telephone(req.shippingInformation.telephoneNumber)
                    .city(req.shippingInformation.city)
                    .zipCode(req.shippingInformation.zipCode)
                    .country(req.shippingInformation.country)
                    .build();

        return Order.builder()
                .orderNo(orderNo)
                .orderer(orderer)
                .receiver(receiver)
                .build();
    }

    @Builder
    @Getter
    public static class Orderer {
        @NotNull
        private final String memberId;
        @NotNull
        private final String name;
    }

    @Builder
    @Getter
    public static class OrderLine {
        @NotNull
        private final OrderProduct orderProduct;
        private final int quantity;
    }

    @Builder
    @Getter
    public static class OrderProduct {
        @NotNull
        private final String productId;
        @NotNull
        private final String productName;
        private final int price;
    }

    @Builder
    @Getter
    public static class ShippingInfo {
        @NotNull
        private final String country;
        @NotNull
        private final String city;
        @NotNull
        private final String zipCode;
        @NotNull
        private final String telephoneNumber;
        @NotNull
        private final String receiver;
    }
}
