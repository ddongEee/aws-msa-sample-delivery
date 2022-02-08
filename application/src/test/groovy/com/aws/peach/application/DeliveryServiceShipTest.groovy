package com.aws.peach.application

import com.aws.peach.domain.delivery.Delivery
import com.aws.peach.domain.delivery.DeliveryChangeEvent
import com.aws.peach.domain.delivery.DeliveryId
import com.aws.peach.domain.delivery.DeliveryRepository
import com.aws.peach.domain.delivery.DeliveryStatus
import com.aws.peach.domain.delivery.OrderNo
import com.aws.peach.domain.delivery.exception.DeliveryNotFoundException
import com.aws.peach.domain.delivery.exception.DeliveryStateException
import com.aws.peach.domain.support.MessageProducer
import com.aws.peach.domain.test.TestMessage
import spock.lang.Specification

class DeliveryServiceShipTest extends Specification {

    DeliveryId deliveryId = new DeliveryId("123")
    OrderNo orderNo = new OrderNo("o123")
    MessageProducer<String, DeliveryChangeEvent> messageProducer
    MessageProducer<String, TestMessage> messageProducer2

    def setup() {
        messageProducer = Mock()
        messageProducer2 = Mock()
    }

    def stubDeliveryRepository(DeliveryId deliveryId, Delivery retrievedDelivery) {
        DeliveryRepository repository = Stub()
        repository.findById(deliveryId) >> Optional.ofNullable(retrievedDelivery)
        return repository
    }

    def "if delivery not found, throw error"() {
        given:
        Delivery retrievedDelivery = null
        DeliveryRepository repository = stubDeliveryRepository(deliveryId, retrievedDelivery)
        DeliveryService service = new DeliveryService(repository, messageProducer, messageProducer2)

        when:
        service.ship(deliveryId)

        then:
        thrown(DeliveryNotFoundException.class)
    }

    def "if delivery status not 'PACKAGING', abort request"() {
        given:
        DeliveryStatus status = new DeliveryStatus(DeliveryStatus.Type.ORDER_RECEIVED)
        Delivery retrievedDelivery = Delivery.builder().id(deliveryId).status(status).build()
        DeliveryRepository repository = stubDeliveryRepository(deliveryId, retrievedDelivery)
        DeliveryService service = new DeliveryService(repository, messageProducer, messageProducer2)

        when:
        service.ship(deliveryId)

        then:
        thrown(DeliveryStateException.class)
    }

    def "upon success, mark delivery order as 'SHIPPED'"() {
        given:
        Delivery retrievedDelivery = mockRetrievedDelivery(deliveryId, orderNo, DeliveryStatus.Type.PACKAGING)
        DeliveryRepository repository = stubDeliveryRepository(deliveryId, retrievedDelivery)
        DeliveryService service = new DeliveryService(repository, messageProducer, messageProducer2)

        when:
        Delivery result = service.ship(deliveryId)

        then:
        1 * retrievedDelivery.ship()
        1 * messageProducer.send(deliveryId.value, _ as DeliveryChangeEvent)
    }

    Delivery mockRetrievedDelivery(DeliveryId deliveryId, OrderNo orderNo, DeliveryStatus.Type statusType) {
        Delivery retrievedDelivery = Mock()
        retrievedDelivery.getId() >> deliveryId
        retrievedDelivery.getOrderNo() >> orderNo
        retrievedDelivery.getStatus() >> new DeliveryStatus(statusType)
        return retrievedDelivery
    }
}
