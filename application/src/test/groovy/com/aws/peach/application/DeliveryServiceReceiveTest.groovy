package com.aws.peach.application

import com.aws.peach.domain.delivery.Delivery
import com.aws.peach.domain.delivery.DeliveryId
import com.aws.peach.domain.delivery.DeliveryRepository
import com.aws.peach.domain.delivery.OrderNo
import com.aws.peach.domain.delivery.exception.DeliveryAlreadyExistsException
import spock.lang.Specification

class DeliveryServiceReceiveTest extends Specification {

    // test data
    DeliveryId deliveryId = new DeliveryId("123")
    OrderNo orderNo = new OrderNo("oid")
    Order order;

    def setup() {
        Order.Orderer orderer = new Order.Orderer("1", "PeachMan")
        Order.Receiver receiver = Order.Receiver.builder()
                                    .name("Sandy")
                                    .telephone("010-1234-1234")
                                    .city("Seoul")
                                    .zipCode("12345")
                                    .country("South Korea").build()
        order = Order.builder()
                    .orderNo(orderNo)
                    .orderer(orderer)
                    .receiver(receiver).build()
    }

    def "if delivery exists, throw error"() {
        given:
        Delivery existingDelivery = createDelivery(deliveryId)
        DeliveryRepository repository = stubDeliveryRepository(deliveryId, existingDelivery)
        DeliveryService service = new DeliveryService(repository)

        when:
        service.createDeliveryOrder(order)

        then:
        thrown(DeliveryAlreadyExistsException.class)
    }

    def "upon success, save delivery order as 'ORDER_RECEIVED'"() {
        given:
        Delivery existingDelivery = null
        DeliveryRepository repository = stubDeliveryRepository(deliveryId, existingDelivery)
        DeliveryService service = new DeliveryService(repository)

        when:
        DeliveryId did = service.createDeliveryOrder(order)

        then:
        did != null // TODO assert repository.save() called
    }

    private DeliveryRepository stubDeliveryRepository(DeliveryId newDeliveryId, Delivery existingDelivery) {
        DeliveryRepository repository = Stub()
        repository.findByOrderNo(orderNo) >> Optional.ofNullable(existingDelivery)
        repository.save(_ as Delivery) >> createDelivery(newDeliveryId)
        return repository
    }

    private static Delivery createDelivery(DeliveryId deliveryId) {
        return Delivery.builder().id(deliveryId).build();
    }
}
