package com.aws.peach.interfaces.message;

import com.aws.peach.application.CreateDeliveryInput;
import com.aws.peach.domain.delivery.Address;
import com.aws.peach.domain.delivery.OrderNo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderStateChangeMessage {
    private String orderNumber; // TODO validation (non-null fields)
    private String ordererId;
    private String ordererName;
    private List<OrderLineDto> orderLines;
    private String status;
    private String orderDate;
    private ShippingInformationDto shippingInformation;

    @JsonIgnore
    public boolean isPaidStatus() {
        return State.PAID.name().equals(status);
    }

    @JsonIgnore
    public Instant getChangedAt() {
        LocalDate date = LocalDate.parse(this.orderDate, DateTimeFormatter.ISO_DATE);
        return date.atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant();
    }

    private enum State {
        PAID, UNKNOWN;
    }

    static CreateDeliveryInput newCreateDeliveryInput(OrderStateChangeMessage m) {
        List<CreateDeliveryInput.OrderProductDto> orderProductDtos = m.getOrderLines().stream()
                .map(OrderLineDto::newOrderProduct)
                .collect(Collectors.toList());
        CreateDeliveryInput.OrderDto orderDto = CreateDeliveryInput.OrderDto.builder()
                .id(new OrderNo(m.getOrderNumber()))
                .createdAt(m.getChangedAt())
                .ordererId(m.getOrdererId())
                .ordererName(m.getOrdererName())
                .products(orderProductDtos)
                .build();
        Address receiver = ShippingInformationDto.newAddress(m.getShippingInformation());
        return CreateDeliveryInput.builder()
                .order(orderDto)
                .receiver(receiver)
                .build();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class OrderLineDto {
        private OrderProductDto orderProduct;
        private int quantity;

        private static CreateDeliveryInput.OrderProductDto newOrderProduct(OrderLineDto ol) {
            return CreateDeliveryInput.OrderProductDto.builder()
                    .name(ol.getOrderProduct().getProductName())
                    .qty(ol.getQuantity())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class OrderProductDto {
        private String productId;
        private String productName;
        private int price;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ShippingInformationDto {
        private String city;
        private String telephoneNumber;
        private String receiver;
        private String address1;
        private String address2;

        public static Address newAddress(ShippingInformationDto s) {
            return Address.builder()
                    .name(s.getReceiver())
                    .telephone(s.getTelephoneNumber())
                    .city(s.getCity())
                    .address1(s.getAddress1())
                    .address2(s.getAddress2())
                    .build();
        }
    }
}
