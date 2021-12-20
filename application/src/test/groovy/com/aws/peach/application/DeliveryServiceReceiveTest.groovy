package com.aws.peach.application

import com.aws.peach.domain.delivery.Delivery
import com.aws.peach.domain.delivery.DeliveryId
import com.aws.peach.domain.delivery.DeliveryRepository
import com.aws.peach.domain.delivery.Order
import com.aws.peach.domain.delivery.OrderNo
import com.aws.peach.domain.delivery.exception.DeliveryAlreadyExistsException
import spock.lang.Specification

import java.time.Instant

class DeliveryServiceReceiveTest extends Specification {

    // test data
    DeliveryId deliveryId = new DeliveryId("123")
    OrderNo orderNo = new OrderNo("oid")
    CreateDeliveryInput createInput;

    def setup() {
        def orderDto = CreateDeliveryInput.OrderDto.builder()
                .id(orderNo)
                .createdAt(Instant.now())
                .ordererId("1")
                .ordererName("PeachMan")
                .build()
        def receiver = CreateDeliveryInput.Receiver.builder()
                .name("Sandy")
                .telephone("010-1234-1234")
                .city("Seoul")
                .zipCode("12345")
                .country("South Korea").build()
        createInput = CreateDeliveryInput.builder()
                    .order(orderDto)
                    .receiver(receiver).build()
    }

    def "if delivery exists, throw error"() {
        given:
        Delivery existingDelivery = createDelivery(deliveryId, orderNo)
        DeliveryRepository repository = stubDeliveryRepository(deliveryId, existingDelivery)
        DeliveryService service = new DeliveryService(repository)

        when:
        service.createDeliveryOrder(createInput)

        then:
        thrown(DeliveryAlreadyExistsException.class)
    }

    def "upon success, save delivery order as 'ORDER_RECEIVED'"() {
        given:
        Delivery existingDelivery = null
        DeliveryRepository repository = stubDeliveryRepository(deliveryId, existingDelivery)
        DeliveryService service = new DeliveryService(repository)

        when:
        Delivery result = service.createDeliveryOrder(createInput)

        then:
        1 * repository.save(_ as Delivery)
    }

    private DeliveryRepository stubDeliveryRepository(DeliveryId newDeliveryId, Delivery existingDelivery) {
        DeliveryRepository repository = Mock()
        repository.findByOrderNo(orderNo) >> Optional.ofNullable(existingDelivery)
        repository.save(_ as Delivery) >> createDelivery(newDeliveryId, orderNo)
        return repository
    }

    private static Delivery createDelivery(DeliveryId deliveryId, OrderNo orderNo) {
        Order order = Order.builder().no(orderNo).build()
        return Delivery.builder().id(deliveryId).order(order).build();
    }
}
