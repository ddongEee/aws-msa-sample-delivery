package com.aws.peach.interfaces.api;

import com.aws.peach.application.CreateDeliveryInput;
import com.aws.peach.domain.delivery.OrderNo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

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

    public static CreateDeliveryInput newCreateDeliveryInput(ReceiveDeliveryOrderRequest req) {
        List<CreateDeliveryInput.OrderProductDto> orderProducts = req.orderLines.stream()
                .map(OrderLine::newOrderProductDto)
                .collect(Collectors.toList());

        CreateDeliveryInput.OrderDto order = CreateDeliveryInput.OrderDto.builder()
                .id(new OrderNo(req.orderNo))
                .createdAt(Instant.parse(req.orderDate))
                .ordererId(req.orderer.memberId)
                .ordererName(req.orderer.name)
                .products(orderProducts)
                .build();

        CreateDeliveryInput.Receiver receiver = CreateDeliveryInput.Receiver.builder()
                    .name(req.shippingInformation.receiver)
                    .telephone(req.shippingInformation.telephoneNumber)
                    .city(req.shippingInformation.city)
                    .address1(req.shippingInformation.address1)
                    .address2(req.shippingInformation.address2)
                    .build();

        return CreateDeliveryInput.builder()
                .order(order)
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

        public static CreateDeliveryInput.OrderProductDto newOrderProductDto(OrderLine o) {
            return CreateDeliveryInput.OrderProductDto.builder()
                    .name(o.orderProduct.productName)
                    .qty(o.quantity)
                    .build();
        }
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
        private final String city;
        @NotNull
        private final String telephoneNumber;
        @NotNull
        private final String receiver;
        @NotNull
        private final String address1;
        private final String address2;
    }
}
