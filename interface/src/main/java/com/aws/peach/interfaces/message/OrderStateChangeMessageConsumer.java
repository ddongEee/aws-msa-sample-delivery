package com.aws.peach.interfaces.message;

import com.aws.peach.application.CreateDeliveryInput;
import com.aws.peach.application.DeliveryService;
import com.aws.peach.domain.support.MessageConsumer;
import org.springframework.stereotype.Component;

@Component
public class OrderStateChangeMessageConsumer implements MessageConsumer<OrderStateChangeMessage> {

    private final DeliveryService deliveryService;

    public OrderStateChangeMessageConsumer(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Override
    public void consume(OrderStateChangeMessage value) {
        if (value.isPaidStatus()) {
            CreateDeliveryInput input = OrderStateChangeMessage.newCreateDeliveryInput(value);
            this.deliveryService.createDeliveryOrder(input);
        }
    }
}
