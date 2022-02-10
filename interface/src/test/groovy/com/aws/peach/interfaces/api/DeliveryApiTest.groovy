package com.aws.peach.interfaces.api

import com.aws.peach.domain.delivery.DeliveryStatus
import com.aws.peach.interfaces.api.model.DeliveryDetailResponse
import com.aws.peach.interfaces.api.model.ReceiveDeliveryOrderRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import spock.lang.Ignore
import spock.lang.Specification

import static com.aws.peach.interfaces.api.ApiTestUtil.generateNewOrderNo

@Ignore
@ApiTest
class DeliveryApiTest extends Specification {

    @LocalServerPort
    private int port

    @Autowired
    private TestRestTemplate restTemplate

    def "should create delivery"() {
        when:
        def orderNo = generateNewOrderNo()
        def request = createReceiveDeliveryOrderRequest(orderNo)
        def entity = this.restTemplate.postForEntity(url("/delivery"),
                request, DeliveryDetailResponse.class)

        then:
        entity.getStatusCode() == HttpStatus.OK
        entity.getBody().getDeliveryId() != null
        entity.getBody().getOrder().getOrderNo() == orderNo
        entity.getBody().getOrder().getOrdererId() == request.getOrderer().getMemberId()
        entity.getBody().getOrder().getOrdererName() == request.getOrderer().getName()
        entity.getBody().getOrder().getOpenedAt() == request.getOrderDate()
        entity.getBody().getItems().size() == request.getOrderLines().size()
        entity.getBody().getShippingAddress().getName() == request.getShippingInformation().getReceiver()
        entity.getBody().getShippingAddress().getTelephone() == request.getShippingInformation().getTelephoneNumber()
        entity.getBody().getStatus() == DeliveryStatus.Type.ORDER_RECEIVED.name()
    }

    def "should prepare delivery"() {
        given:
        def orderNo = generateNewOrderNo()
        def preEntity = this.restTemplate.postForEntity(url("/delivery" ),
                createReceiveDeliveryOrderRequest(orderNo),
                DeliveryDetailResponse.class)
        def deliveryId = preEntity.getBody().getDeliveryId()

        when:
        def entity = this.restTemplate.exchange(url("/delivery/" + deliveryId + "/prepare"), HttpMethod.PUT,
                null, DeliveryDetailResponse.class)

        then:
        entity.getStatusCode() == HttpStatus.OK
        entity.getBody().getDeliveryId() == deliveryId
        entity.getBody().getOrder().getOrderNo() == orderNo
        entity.getBody().getStatus() == DeliveryStatus.Type.PREPARING.name()
    }

    def "should pack delivery"() {
        given:
        def orderNo = generateNewOrderNo()
        def preEntity = this.restTemplate.postForEntity(url("/delivery" ),
                createReceiveDeliveryOrderRequest(orderNo),
                DeliveryDetailResponse.class)
        def deliveryId = preEntity.getBody().getDeliveryId()
        this.restTemplate.put(url("/delivery/" + deliveryId + "/prepare"), null)

        when:
        def entity = this.restTemplate.exchange(url("/delivery/" + deliveryId + "/package"), HttpMethod.PUT,
                null, DeliveryDetailResponse.class)

        then:
        entity.getStatusCode() == HttpStatus.OK
        entity.getBody().getDeliveryId() == deliveryId
        entity.getBody().getOrder().getOrderNo() == orderNo
        entity.getBody().getStatus() == DeliveryStatus.Type.PACKAGING.name()
    }

    def "should ship delivery"() {
        given:
        def orderNo = generateNewOrderNo()
        def preEntity = this.restTemplate.postForEntity(url("/delivery" ),
                createReceiveDeliveryOrderRequest(orderNo),
                DeliveryDetailResponse.class)
        def deliveryId = preEntity.getBody().getDeliveryId()
        this.restTemplate.put(url("/delivery/" + deliveryId + "/prepare"), null)
        this.restTemplate.put(url("/delivery/" + deliveryId + "/package"), null)

        when:
        def entity = this.restTemplate.exchange(url("/delivery/" + deliveryId + "/ship"), HttpMethod.PUT,
                null, DeliveryDetailResponse.class)

        then:
        entity.getStatusCode() == HttpStatus.OK
        entity.getBody().getDeliveryId() == deliveryId
        entity.getBody().getOrder().getOrderNo() == orderNo
        entity.getBody().getStatus() == DeliveryStatus.Type.SHIPPED.name()
    }

    private String url(String suffix) {
        return ApiTestUtil.url(this.port, suffix)
    }

    private static def createReceiveDeliveryOrderRequest(String orderNo) {
        def orderer = ReceiveDeliveryOrderRequest.Orderer.builder()
                .memberId("PeachMan")
                .name("Albert")
                .build()
        def orderLines = Arrays.asList(
                createOrderLine("SKU1", "white peach", 5000, 1),
                createOrderLine("SKU2", "yellow peach", 4000, 1)
        )
        def shippingInfo = ReceiveDeliveryOrderRequest.ShippingInfo.builder()
                .city("Seoul")
                .telephoneNumber("010-1234-1234")
                .receiver("Benjamin")
                .address1("Namsan-ro 110")
                .address2("Zeus Bld. 1st Fl")
                .build()
        return ReceiveDeliveryOrderRequest.builder()
                .orderNo(orderNo)
                .orderer(orderer)
                .orderLines(orderLines)
                .orderState("PAID")
                .orderDate("2021-12-16T05:29:23Z")
                .shippingInformation(shippingInfo)
                .build()
    }

    private static def createOrderLine(String productId, String productName, int price, int qty) {
        ReceiveDeliveryOrderRequest.OrderProduct product = ReceiveDeliveryOrderRequest.OrderProduct.builder()
                .productId(productId)
                .productName(productName)
                .price(price)
                .build()
        return ReceiveDeliveryOrderRequest.OrderLine.builder()
                    .orderProduct(product)
                    .quantity(qty)
                    .build()
    }
}
