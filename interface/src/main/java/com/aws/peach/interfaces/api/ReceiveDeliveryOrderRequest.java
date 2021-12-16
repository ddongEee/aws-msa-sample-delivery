package com.aws.peach.interfaces.api;

import com.aws.peach.application.Order;
import com.aws.peach.domain.delivery.OrderNo;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@ToString
@Getter
public class ReceiveDeliveryOrderRequest {
    @NotNull
    private String orderNo;
    @NotNull
    private Orderer orderer;
    @NotEmpty
    private List<OrderLine> orderLines;
    private String orderState;
    @NotNull
    private String orderDate;
    @NotNull
    private ShippingInfo shippingInformation;

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

    @Getter
    public static class Orderer {
        @NotNull
        private String memberId;
        @NotNull
        private String name;
    }

    @Getter
    public static class OrderLine {
        @NotNull
        private OrderProduct orderProduct;
        private int quantity;
    }

    @Getter
    public static class OrderProduct {
        @NotNull
        private String productId;
        @NotNull
        private String productName;
        private int price;
    }

    @Getter
    public static class ShippingInfo {
        @NotNull
        private String country;
        @NotNull
        private String city;
        @NotNull
        private String zipCode;
        @NotNull
        private String telephoneNumber;
        @NotNull
        private String receiver;
    }
}
