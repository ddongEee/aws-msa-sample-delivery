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
import spock.lang.Specification

class DeliveryServicePrepareTest extends Specification {

    DeliveryId deliveryId = new DeliveryId("123")
    OrderNo orderNo = new OrderNo("o123")
    MessageProducer<String, DeliveryChangeEvent> messageProducer;

    def setup() {
        messageProducer = Mock()
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
        DeliveryService service = new DeliveryService(repository, messageProducer)

        when:
        service.prepare(deliveryId)

        then:
        thrown(DeliveryNotFoundException.class)
    }

    def "if delivery status not 'ORDER_RECEIVED', abort request"() {
        given:
        DeliveryStatus status = new DeliveryStatus(DeliveryStatus.Type.PACKAGING)
        Delivery retrievedDelivery = Delivery.builder().id(deliveryId).status(status).build()
        DeliveryRepository repository = stubDeliveryRepository(deliveryId, retrievedDelivery)
        DeliveryService service = new DeliveryService(repository, messageProducer)

        when:
        service.prepare(deliveryId)

        then:
        thrown(DeliveryStateException.class)
    }

    def "upon success, mark delivery order as 'PREPARING'"() {
        given:
        Delivery retrievedDelivery = mockRetrievedDelivery(deliveryId, orderNo, DeliveryStatus.Type.ORDER_RECEIVED)
        DeliveryRepository repository = stubDeliveryRepository(deliveryId, retrievedDelivery)
        DeliveryService service = new DeliveryService(repository, messageProducer)

        when:
        Delivery result = service.prepare(deliveryId)

        then:
        1 * retrievedDelivery.prepare()
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
