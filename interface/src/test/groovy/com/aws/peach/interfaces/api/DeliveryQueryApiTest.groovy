package com.aws.peach.interfaces.api

import com.aws.peach.domain.delivery.DeliveryStatus
import com.aws.peach.interfaces.api.model.DeliveryDetailResponse
import com.aws.peach.interfaces.api.model.DeliverySearchResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification

import java.util.stream.Collectors

@ApiTest
@Sql("/sql/init_delivery_query_api_test.sql")
class DeliveryQueryApiTest extends Specification {

    @LocalServerPort
    private int port

    @Autowired
    private TestRestTemplate restTemplate

    def "should query delivery with delivery_id"() {
        given:
        def deliveryId = 'ID_001'

        when:
        def entity = this.restTemplate.getForEntity(url("/delivery/" + deliveryId), DeliveryDetailResponse.class)

        then:
        entity.getStatusCode() == HttpStatus.OK
        entity.getBody().getDeliveryId() == deliveryId
        entity.getBody().getStatus() == DeliveryStatus.Type.ORDER_RECEIVED.name()
    }

    def "should query delivery with order_no"() {
        given:
        def orderNo = 'NO_001'

        when:
        def entity = this.restTemplate.getForEntity(url("/delivery?orderNo=" + orderNo), DeliveryDetailResponse.class)

        then:
        entity.getStatusCode() == HttpStatus.OK
        entity.getBody().getOrder().getOrderNo() == orderNo
        entity.getBody().getStatus() == DeliveryStatus.Type.ORDER_RECEIVED.name()
    }

    def "should paginate delivery search result"(int pageNo, int pageSize, List<String> searchedIds) {
        when:
        def url = url("/delivery/searches?pageNo=" + pageNo + "&pageSize=" + pageSize)
        def queryResult =  this.restTemplate.getForEntity(url, DeliverySearchResponse.class)
        DeliverySearchResponse response = queryResult.getBody()
        List<String> deliveryIds = response.getSearchResult().stream()
                                        .map(DeliverySearchResponse.Search::getDeliveryId)
                                        .collect(Collectors.toList())

        then:
        response.getSearchResult().size() == pageSize
        deliveryIds.sort() == searchedIds.sort()

        where:
        pageNo   | pageSize | searchedIds
        0        | 3        | ['ID_001', 'ID_002', 'ID_003']
        1        | 3        | ['ID_004', 'ID_005', 'ID_006']
        1        | 4        | ['ID_005', 'ID_006', 'ID_007', 'ID_008']
    }

    def "should query deliveries by state"(String state, int count) {
        when:
        def queryResult =  this.restTemplate.getForEntity(url("/delivery/searches?state=" + state), DeliverySearchResponse.class)

        then:
        queryResult.getBody().getSearchResult().size() == count

        where:
        state       | count
        'paid'      | 3
        'preparing' | 2
        'packaging' | 1
        'shipped'   | 2
        'delivered' | 2
    }

    private String url(String suffix) {
        return ApiTestUtil.url(this.port, suffix);
    }
}
