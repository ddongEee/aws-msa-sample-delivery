package com.aws.peach.application

import com.aws.peach.domain.delivery.Delivery
import com.aws.peach.domain.delivery.DeliveryId
import com.aws.peach.domain.delivery.DeliveryRepository
import com.aws.peach.domain.delivery.DeliveryStatus
import com.aws.peach.domain.delivery.OrderNo
import com.aws.peach.domain.delivery.exception.DeliveryNotFoundException
import com.aws.peach.domain.delivery.exception.DeliveryPrepareException
import spock.lang.Specification

class DeliveryServicePackageTest extends Specification {

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
        service.pack(orderNo)

        then:
        thrown(DeliveryNotFoundException.class)
    }

    def "if delivery status not 'PREPARING', abort request"() {
        given:
        OrderNo orderNo = new OrderNo("oid")
        DeliveryRepository repository = stubDeliveryRepository(orderNo,
                Delivery.builder().orderNo(orderNo).status(DeliveryStatus.SHIPPED).build())
        DeliveryService service = new DeliveryService(repository)

        when:
        service.pack(orderNo)

        then:
        thrown(DeliveryPrepareException.class)
    }

    def "upon success, mark delivery order as 'PACKAGING'"() {
        given:
        DeliveryId did = new DeliveryId("123")
        OrderNo orderNo = new OrderNo("1")
        Delivery delivery = Mock();
        delivery.getId() >> did
        DeliveryRepository repository = stubDeliveryRepository(orderNo, delivery)
        DeliveryService service = new DeliveryService(repository)

        when:
        DeliveryId did2 = service.pack(orderNo)

        then:
        1 * delivery.pack()
    }
}
