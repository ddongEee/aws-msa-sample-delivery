package com.aws.peach.infrastructure.dummy

import com.aws.peach.domain.delivery.Delivery
import com.aws.peach.domain.delivery.OrderNo
import spock.lang.Specification

class DeliveryDummyRepositoryTest extends Specification {

    // test data
    OrderNo orderNo;
    Delivery delivery;

    def setup() {
        orderNo = new OrderNo("123")
        delivery = Delivery.builder().orderNo(orderNo).build()
    }

    def "should save delivery item"() {
        given:
        DeliveryDummyRepository repository = new DeliveryDummyRepository()

        when:
        delivery = repository.save(delivery)

        then:
        delivery.getId() != null
        delivery.getOrderNo() == new OrderNo("123")
        repository.map.get(new OrderNo("123")) == delivery
    }

    def "should retrieve saved delivery item"() {
        given:
        Map<OrderNo, Delivery> map = new HashMap<>()
        map.put(orderNo, delivery)
        DeliveryDummyRepository repository = new DeliveryDummyRepository(map)

        when:
        Delivery delivery1 = repository.findByOrderNo(orderNo).get()

        then:
        delivery1.getOrderNo() == new OrderNo("123")
    }

    def "when delivery not found, should return empty optional"() {
        given:
        Map<OrderNo, Delivery> map = new HashMap<>()
        map.put(orderNo, delivery)
        DeliveryDummyRepository repository = new DeliveryDummyRepository(map)

        when:
        Optional<Delivery> deliveryOpt = repository.findByOrderNo(new OrderNo("234"))

        then:
        deliveryOpt.isEmpty()
    }
}
