package com.aws.peach.application;

import com.aws.peach.domain.delivery.Delivery;
import com.aws.peach.domain.delivery.DeliveryId;
import com.aws.peach.domain.delivery.DeliveryRepository;
import com.aws.peach.domain.delivery.OrderNo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeliveryQueryService {

    private final DeliveryRepository repository;

    public DeliveryQueryService(DeliveryRepository repository) {
        this.repository = repository;
    }

    public Optional<Delivery> getDelivery(DeliveryId deliveryId) {
        return this.repository.findById(deliveryId);
    }

    public Optional<Delivery> getDelivery(OrderNo orderNo) {
        return this.repository.findByOrderNo(orderNo);
    }

//    public List<DeliveryDetail> getDeliveries() {
        // - shipment id 로 배송 이력을 조회할 수 있다
        // - order id 로 배송 이력을 조회할 수 있다
        // - 시간순으로 배송 이력을 정렬할 수 있다
        // - 상태 기준으로 배송 이력을 조회할 수 있다
//    }
}
