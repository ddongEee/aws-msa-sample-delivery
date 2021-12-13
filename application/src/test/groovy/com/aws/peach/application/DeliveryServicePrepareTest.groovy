package com.aws.peach.application

import com.aws.peach.domain.delivery.Delivery
import com.aws.peach.domain.delivery.DeliveryRepository
import com.aws.peach.domain.delivery.DeliveryStatus
import com.aws.peach.domain.delivery.OrderNo
import com.aws.peach.domain.delivery.exception.DeliveryNotFoundException
import com.aws.peach.domain.delivery.exception.DeliveryPrepareException
import spock.lang.Specification

class DeliveryServicePrepareTest extends Specification {

    def stubDeliveryRepository(OrderNo orderNo, Delivery delivery) {
        DeliveryRepository repository = Stub()
        repository.findByOrderNo(orderNo) >> Optional.ofNullable(delivery)
        return repository
    }

    def "if delivery not found, throw error"() {
        given:
        OrderNo orderNo = new OrderNo("oid")
        DeliveryRepository repository = stubDeliveryRepository(orderNo, null)
        DeliveryService service = new DeliveryService(repository)

        when:
        service.prepare(orderNo)

        then:
        thrown(DeliveryNotFoundException.class)
    }

    def "if delivery status not 'ORDER_RECEIVED', abort request"() {
        given:
        OrderNo orderNo = new OrderNo("oid")
        DeliveryRepository repository = stubDeliveryRepository(orderNo,
                new Delivery.Builder(orderNo).status(DeliveryStatus.READY_SHIPPING).build())
        DeliveryService service = new DeliveryService(repository)

        when:
        service.prepare(orderNo)

        then:
        thrown(DeliveryPrepareException.class)
    }

    def "upon success, mark delivery order as 'PREPARING'"() {
        given:
        OrderNo orderNo = new OrderNo("1")
        DeliveryRepository repository = stubDeliveryRepository(orderNo,
                new Delivery.Builder(orderNo).build())
        DeliveryService service = new DeliveryService(repository)

        when:
        Delivery delivery = service.prepare(orderNo)

        then:
        delivery.status == DeliveryStatus.PREPARING
    }
}
