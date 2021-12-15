package com.aws.peach.application

import com.aws.peach.domain.delivery.Delivery
import com.aws.peach.domain.delivery.DeliveryId
import com.aws.peach.domain.delivery.DeliveryRepository
import com.aws.peach.domain.delivery.OrderNo
import com.aws.peach.domain.delivery.exception.DeliveryAlreadyExistsException
import spock.lang.Specification

class DeliveryServiceReceiveTest extends Specification {

    // test data
    OrderNo orderNo;
    Order order;

    def setup() {
        orderNo = new OrderNo("oid")

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

    def stubDeliveryRepository(Delivery delivery) {
        DeliveryRepository repository = Stub()
        repository.findByOrderNo(orderNo) >> Optional.ofNullable(delivery)
        return repository
    }

    def "if delivery exists, throw error"() {
        given:
        DeliveryRepository repository = stubDeliveryRepository(Mock(Delivery.class))
        DeliveryService service = new DeliveryService(repository)

        when:
        service.createDeliveryOrder(order)

        then:
        thrown(DeliveryAlreadyExistsException.class)
    }

    def "upon success, save delivery order as 'ORDER_RECEIVED'"() {
        given:
        DeliveryRepository repository = stubDeliveryRepository(null)
        Delivery saveResult = Delivery.builder().id(new DeliveryId("1")).build()
        repository.save(_ as Delivery) >> saveResult
        DeliveryService service = new DeliveryService(repository)

        when:
        DeliveryId did = service.createDeliveryOrder(order)

        then:
        did != null // TODO assert repository.save() called
    }
}
