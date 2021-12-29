package com.aws.peach.interfaces.message

import com.aws.peach.application.CreateDeliveryInput
import com.aws.peach.application.DeliveryService
import com.aws.peach.domain.order.OrderStateChangeMessage
import spock.lang.Specification

class OrderStateChangeMessageConsumerTest extends Specification {


    DeliveryService deliveryService

    def setup() {
        deliveryService = Mock(DeliveryService)
    }

    def "should consume valid message and create delivery"() {
        given:
        OrderStateChangeMessage msg = createOrderStateChangeMessage(OrderStateChangeMessage.State.PAID)
        OrderStateChangeMessageConsumer consumer = createOrderStateChangeMessageConsumer()

        when:
        consumer.consume(msg)

        then:
        1 * deliveryService.createDeliveryOrder({
            assert it instanceof CreateDeliveryInput
            it.order.id.value == msg.getOrderNumber()
            it.order.createdAt != null
            it.order.ordererId == msg.getOrdererId()
            it.order.ordererName == msg.getOrdererName()
            it.order.products.size() == msg.getOrderLines().size()
            it.receiver.name == msg.getShippingInformation().getReceiver()
        })
    }

    static OrderStateChangeMessage createOrderStateChangeMessage(OrderStateChangeMessage.State state) {
        OrderStateChangeMessage.OrderProductDto peach =
                new OrderStateChangeMessage.OrderProductDto("YP", "Yellow Peach", 100)
        List<OrderStateChangeMessage.OrderLineDto> orderLines =
                Collections.singletonList(new OrderStateChangeMessage.OrderLineDto(peach, 10))
        OrderStateChangeMessage.ShippingInformationDto shipping =
                OrderStateChangeMessage.ShippingInformationDto.builder()
                        .receiver("Alice")
                        .telephoneNumber("010-1234-1234")
                        .city("Seoul")
                        .address1("Teheran-ro 100")
                        .address2("Royal Garden")
                        .build()
        return OrderStateChangeMessage.builder()
                .orderNumber("1")
                .ordererId("PeachMan")
                .ordererName("Benjamin")
                .orderLines(orderLines)
                .status(state.name())
                .orderDate("2021-12-27")
                .shippingInformation(shipping)
                .build()
    }

    OrderStateChangeMessageConsumer createOrderStateChangeMessageConsumer() {
        return new OrderStateChangeMessageConsumer(deliveryService)
    }
}
