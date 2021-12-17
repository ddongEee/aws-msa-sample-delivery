package com.aws.peach.infrastructure.dummy

import com.aws.peach.domain.delivery.Delivery
import com.aws.peach.domain.delivery.DeliveryId
import com.aws.peach.domain.delivery.OrderNo
import spock.lang.Specification

class DeliveryDummyRepositoryTest extends Specification {

    // test data
    OrderNo orderNo;
    DeliveryId deliveryId;
    Delivery delivery;

    def setup() {
        orderNo = new OrderNo("o123")
        deliveryId = new DeliveryId("d123")
        delivery = Delivery.builder().id(deliveryId).orderNo(orderNo).build()
    }

    def "should save delivery"() {
        given:
        DeliveryDummyRepository repository = new DeliveryDummyRepository()

        when:
        Delivery savedDelivery = repository.save(delivery)

        then:
        repository.orderNo2Delivery.get(orderNo) == delivery
        repository.deliveryId2Delivery.get(savedDelivery.getId()) == delivery
    }

    def "should find delivery by order_no"() {
        given:
        List<Delivery> initData = Collections.singletonList(delivery)
        DeliveryDummyRepository repository = new DeliveryDummyRepository(initData)

        when:
        Delivery delivery1 = repository.findByOrderNo(orderNo).get()

        then:
        delivery1.getOrderNo() == orderNo
    }

    def "when delivery not found by order_no, should return empty optional"() {
        given:
        List<Delivery> initData = Collections.singletonList(delivery)
        DeliveryDummyRepository repository = new DeliveryDummyRepository(initData)

        when:
        Optional<Delivery> deliveryOpt = repository.findByOrderNo(new OrderNo("o234"))

        then:
        deliveryOpt.isEmpty()
    }

    def "should find delivery by delivery_id"() {
        given:
        List<Delivery> initData = Collections.singletonList(delivery)
        DeliveryDummyRepository repository = new DeliveryDummyRepository(initData)

        when:
        Delivery delivery1 = repository.findById(deliveryId).get()

        then:
        delivery1.getId() == deliveryId
    }

    def "when delivery not found by delivery_id, should return empty optional"() {
        given:
        List<Delivery> initData = Collections.singletonList(delivery)
        DeliveryDummyRepository repository = new DeliveryDummyRepository(initData)

        when:
        Optional<Delivery> deliveryOpt = repository.findById(new DeliveryId("d234"))

        then:
        deliveryOpt.isEmpty()
    }
}
