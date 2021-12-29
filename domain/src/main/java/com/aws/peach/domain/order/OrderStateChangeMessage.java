package com.aws.peach.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    enum State {
        PAID, UNKNOWN;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PACKAGE)
    public static class OrderLineDto {
        private OrderProductDto orderProduct;
        private int quantity;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PACKAGE)
    public static class OrderProductDto {
        private String productId;
        private String productName;
        private int price;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ShippingInformationDto {
        private String city;
        private String telephoneNumber;
        private String receiver;
        private String address1;
        private String address2;
    }
}
