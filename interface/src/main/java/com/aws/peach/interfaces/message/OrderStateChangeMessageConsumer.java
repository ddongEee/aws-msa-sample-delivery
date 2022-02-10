package com.aws.peach.interfaces.message;

import com.aws.peach.application.CreateDeliveryInput;
import com.aws.peach.application.DeliveryService;
import com.aws.peach.domain.delivery.Address;
import com.aws.peach.domain.delivery.OrderNo;
import com.aws.peach.domain.order.OrderStateChangeMessage;
import com.aws.peach.domain.support.Message;
import com.aws.peach.domain.support.MessageConsumer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderStateChangeMessageConsumer implements MessageConsumer<OrderStateChangeMessage> {

    private final DeliveryService deliveryService;

    public OrderStateChangeMessageConsumer(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Override
    public void consume(Message<OrderStateChangeMessage> message) {
        // todo message validation
        OrderStateChangeMessage data = message.getPayload();
        if (data.isPaidStatus()) {
            CreateDeliveryInput input = OrderStateChangeMessageConsumer.Mapper.newCreateDeliveryInput(data);
            this.deliveryService.createDeliveryOrder(input);
        }
    }

    static class Mapper {
        public static CreateDeliveryInput newCreateDeliveryInput(OrderStateChangeMessage m) {
            List<CreateDeliveryInput.OrderProductDto> orderProductDtos = m.getOrderLines().stream()
                    .map(OrderStateChangeMessageConsumer.Mapper::newOrderProduct)
                    .collect(Collectors.toList());
            CreateDeliveryInput.OrderDto orderDto = CreateDeliveryInput.OrderDto.builder()
                    .id(new OrderNo(m.getOrderNumber()))
                    .createdAt(m.getChangedAt())
                    .ordererId(m.getOrdererId())
                    .ordererName(m.getOrdererName())
                    .products(orderProductDtos)
                    .build();
            Address receiver = OrderStateChangeMessageConsumer.Mapper.newAddress(m.getShippingInformation());
            return CreateDeliveryInput.builder()
                    .order(orderDto)
                    .receiver(receiver)
                    .build();
        }

        private static CreateDeliveryInput.OrderProductDto newOrderProduct(OrderStateChangeMessage.OrderLineDto ol) {
            return CreateDeliveryInput.OrderProductDto.builder()
                    .name(ol.getOrderProduct().getProductName())
                    .quantity(ol.getQuantity())
                    .build();
        }

        private static Address newAddress(OrderStateChangeMessage.ShippingInformationDto s) {
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
