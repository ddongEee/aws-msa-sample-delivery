package com.aws.peach.application

import com.aws.peach.domain.delivery.Delivery
import com.aws.peach.domain.delivery.DeliveryId
import com.aws.peach.domain.delivery.DeliveryRepository
import com.aws.peach.domain.delivery.DeliveryStatus
import com.aws.peach.domain.delivery.exception.DeliveryNotFoundException
import com.aws.peach.domain.delivery.exception.DeliveryStateException
import spock.lang.Specification

class DeliveryServiceShipTest extends Specification {

    DeliveryId deliveryId = new DeliveryId("123")

    def stubDeliveryRepository(DeliveryId deliveryId, Delivery retrievedDelivery) {
        DeliveryRepository repository = Stub()
        repository.findById(deliveryId) >> Optional.ofNullable(retrievedDelivery)
        return repository
    }

    def "if delivery not found, throw error"() {
        given:
        Delivery retrievedDelivery = null
        DeliveryRepository repository = stubDeliveryRepository(deliveryId, retrievedDelivery)
        DeliveryService service = new DeliveryService(repository)

        when:
        service.ship(deliveryId)

        then:
        thrown(DeliveryNotFoundException.class)
    }

    def "if delivery status not 'PACKAGING', abort request"() {
        given:
        Delivery retrievedDelivery = Delivery.builder().id(deliveryId).status(DeliveryStatus.ORDER_RECEIVED).build()
        DeliveryRepository repository = stubDeliveryRepository(deliveryId, retrievedDelivery)
        DeliveryService service = new DeliveryService(repository)

        when:
        service.ship(deliveryId)

        then:
        thrown(DeliveryStateException.class)
    }

    def "upon success, mark delivery order as 'SHIPPED'"() {
        given:
        Delivery retrievedDelivery = Mock();
        retrievedDelivery.getId() >> deliveryId
        DeliveryRepository repository = stubDeliveryRepository(deliveryId, retrievedDelivery)
        DeliveryService service = new DeliveryService(repository)

        when:
        Delivery result = service.ship(deliveryId)

        then:
        1 * retrievedDelivery.ship()
    }
}
