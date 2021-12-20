package com.aws.peach.interfaces.api


import com.aws.peach.domain.delivery.DeliveryStatus
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeliveryApiTest extends Specification {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static int orderNo = 0

    private static String generateNewOrderNo() {
        orderNo += 1
        return String.valueOf(orderNo)
    }

    def "should create delivery"() {
        when:
        def orderNo = generateNewOrderNo()
        def entity = this.restTemplate.postForEntity(url("/delivery"),
                createReceiveDeliveryOrderRequest(orderNo),
                DeliveryDetailResponse.class)

        then:
        entity.getStatusCode() == HttpStatus.OK
        entity.getBody().getDeliveryId() != null
        entity.getBody().getOrderNo() == orderNo
        entity.getBody().getStatus() == DeliveryStatus.ORDER_RECEIVED.name()
    }

    def "should query delivery with delivery_id"() {
        given:
        def orderNo = generateNewOrderNo()
        def preEntity = this.restTemplate.postForEntity(url("/delivery"),
                createReceiveDeliveryOrderRequest(orderNo),
                DeliveryDetailResponse.class)
        def deliveryId = preEntity.getBody().getDeliveryId()

        when:
        def entity = this.restTemplate.getForEntity(url("/delivery/" + deliveryId), DeliveryDetailResponse.class)

        then:
        entity.getStatusCode() == HttpStatus.OK
        entity.getBody().getDeliveryId() == deliveryId
        entity.getBody().getOrderNo() == orderNo
        entity.getBody().getStatus() == DeliveryStatus.ORDER_RECEIVED.name()
    }

    def "should query delivery with order_no"() {
        given:
        def orderNo = generateNewOrderNo()
        def preEntity = this.restTemplate.postForEntity(url("/delivery" ),
                createReceiveDeliveryOrderRequest(orderNo),
                DeliveryDetailResponse.class)
        def deliveryId = preEntity.getBody().getDeliveryId()

        when:
        def entity = this.restTemplate.getForEntity(url("/delivery?orderNo=" + orderNo), DeliveryDetailResponse.class)

        then:
        entity.getStatusCode() == HttpStatus.OK
        entity.getBody().getDeliveryId() == deliveryId
        entity.getBody().getOrderNo() == orderNo
        entity.getBody().getStatus() == DeliveryStatus.ORDER_RECEIVED.name()
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
        entity.getBody().getOrderNo() == orderNo
        entity.getBody().getStatus() == DeliveryStatus.PREPARING.name()
    }

    def "should pack delivery"() {
        given:
        def orderNo = generateNewOrderNo()
        def preEntity = this.restTemplate.postForEntity(url("/delivery" ),
                createReceiveDeliveryOrderRequest(orderNo),
                DeliveryResponse.class)
        def deliveryId = preEntity.getBody().getDeliveryId()
        this.restTemplate.put(url("/delivery/" + deliveryId + "/prepare"), null)

        when:
        def entity = this.restTemplate.exchange(url("/delivery/" + deliveryId + "/package"), HttpMethod.PUT,
                null, DeliveryDetailResponse.class)

        then:
        entity.getStatusCode() == HttpStatus.OK
        entity.getBody().getDeliveryId() == deliveryId
        entity.getBody().getOrderNo() == orderNo
        entity.getBody().getStatus() == DeliveryStatus.PACKAGING.name()
    }

    def "should ship delivery"() {
        given:
        def orderNo = generateNewOrderNo()
        def preEntity = this.restTemplate.postForEntity(url("/delivery" ),
                createReceiveDeliveryOrderRequest(orderNo),
                DeliveryResponse.class)
        def deliveryId = preEntity.getBody().getDeliveryId()
        this.restTemplate.put(url("/delivery/" + deliveryId + "/prepare"), null)
        this.restTemplate.put(url("/delivery/" + deliveryId + "/package"), null)

        when:
        def entity = this.restTemplate.exchange(url("/delivery/" + deliveryId + "/ship"), HttpMethod.PUT,
                null, DeliveryDetailResponse.class)

        then:
        entity.getStatusCode() == HttpStatus.OK
        entity.getBody().getDeliveryId() == deliveryId
        entity.getBody().getOrderNo() == orderNo
        entity.getBody().getStatus() == DeliveryStatus.SHIPPED.name()
    }

    def url(String suffix) {
        return "http://localhost:" + port + suffix
    }

    static def createReceiveDeliveryOrderRequest(String orderNo) {
        def orderer = ReceiveDeliveryOrderRequest.Orderer.builder()
                .memberId("PeachMan")
                .name("Albert")
                .build()
        def orderLines = Arrays.asList(
                createOrderLine("SKU1", "white peach", 5000, 1),
                createOrderLine("SKU2", "yellow peach", 4000, 1)
        );
        def shippingInfo = ReceiveDeliveryOrderRequest.ShippingInfo.builder()
                .country("South Korea")
                .city("Seoul")
                .zipCode("12345")
                .telephoneNumber("010-1234-1234")
                .receiver("Benjamin")
                .build();
        return ReceiveDeliveryOrderRequest.builder()
                .orderNo(orderNo)
                .orderer(orderer)
                .orderLines(orderLines)
                .orderState("PAID")
                .orderDate("2021-12-16T05:29:23Z")
                .shippingInformation(shippingInfo)
                .build()
    }

    static def createOrderLine(String productId, String productName, int price, int qty) {
        ReceiveDeliveryOrderRequest.OrderProduct product = ReceiveDeliveryOrderRequest.OrderProduct.builder()
                .productId(productId)
                .productName(productName)
                .price(price)
                .build()
        return ReceiveDeliveryOrderRequest.OrderLine.builder()
                    .orderProduct(product)
                    .quantity(qty)
                    .build();
    }
}
