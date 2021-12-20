package com.aws.peach.application

import com.aws.peach.domain.delivery.Address
import com.aws.peach.domain.delivery.Delivery
import com.aws.peach.domain.delivery.DeliveryId
import com.aws.peach.domain.delivery.DeliveryRepository
import com.aws.peach.domain.delivery.DeliveryStatus
import com.aws.peach.domain.delivery.Order
import com.aws.peach.domain.delivery.OrderNo
import com.aws.peach.domain.delivery.exception.DeliveryAlreadyExistsException
import spock.lang.Specification

import java.time.Instant

class DeliveryServiceReceiveTest extends Specification {

    // test data
    OrderNo orderNo = new OrderNo("oid")
    CreateDeliveryInput createInput

    def setup() {
        Instant orderCreatedAt = Instant.now()
        String ordererId = "1"
        String ordererName = "PeachMan"
        List<CreateDeliveryInput.OrderProductDto> orderProducts = Arrays.asList(
                CreateDeliveryInput.OrderProductDto.builder().name("YP").qty(1).build(),
                CreateDeliveryInput.OrderProductDto.builder().name("WP").qty(2).build()
        )

        def orderDto = CreateDeliveryInput.OrderDto.builder()
                .id(orderNo)
                .createdAt(orderCreatedAt)
                .ordererId(ordererId)
                .ordererName(ordererName)
                .products(orderProducts)
                .build()
        def receiver = Address.builder()
                .name("Sandy")
                .telephone("010-1234-1234")
                .city("Seoul")
                .address1("Teheran-ro 100")
                .address2("Royal Building 23rd floor")
                .build()
        createInput = CreateDeliveryInput.builder()
                    .order(orderDto)
                    .receiver(receiver).build()
    }

    def "if delivery exists, throw error"() {
        given:
        Delivery existingDelivery = fakeExistingDelivery(orderNo)
        DeliveryRepository repository = createTestRepository(existingDelivery)
        DeliveryService service = new DeliveryService(repository)

        when:
        service.createDeliveryOrder(createInput)

        then:
        thrown(DeliveryAlreadyExistsException.class)
    }

    def "upon success, save delivery order as 'ORDER_RECEIVED'"() {
        given:
        Delivery existingDelivery = null
        DeliveryRepository repository = createTestRepository(existingDelivery)
        DeliveryService service = new DeliveryService(repository)

        when:
        Delivery result = service.createDeliveryOrder(createInput)

        then:
        result.id != null
        result.order.no == createInput.order.id
        result.order.openedAt == createInput.order.createdAt
        result.order.orderer.id == createInput.order.ordererId
        result.order.orderer.name == createInput.order.ordererName
        // TODO test sender parsing
        result.receiver.name == createInput.receiver.name
        result.receiver.city == createInput.receiver.city
        result.receiver.telephone == createInput.receiver.telephone
        result.receiver.address1 == createInput.receiver.address1
        result.receiver.address2 == createInput.receiver.address2
        result.status.type == DeliveryStatus.Type.ORDER_RECEIVED
        result.items.items.get(0).name == createInput.order.products.get(0).name
        result.items.items.get(0).qty == createInput.order.products.get(0).qty
        result.items.items.get(1).name == createInput.order.products.get(1).name
        result.items.items.get(1).qty == createInput.order.products.get(1).qty
    }

    private static DeliveryRepository createTestRepository(Delivery existingDelivery) {
        List<Delivery> initData = existingDelivery == null ? new ArrayList<Delivery>() : Arrays.asList(existingDelivery)
        return new DeliveryTestRepository(initData)
    }

    private static Delivery fakeExistingDelivery(OrderNo orderNo) {
        DeliveryId existingDeliveryId = new DeliveryId("123")
        Order order = Order.builder().no(orderNo).build()
        return Delivery.builder().id(existingDeliveryId).order(order).build()
    }
}
