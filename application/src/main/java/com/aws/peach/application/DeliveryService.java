package com.aws.peach.application;

import com.aws.peach.domain.delivery.Delivery;
import com.aws.peach.domain.delivery.DeliveryRepository;
import com.aws.peach.domain.delivery.OrderNo;
import com.aws.peach.domain.delivery.exception.DeliveryNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DeliveryService { // 백오피스

    private final DeliveryRepository repository;

    public DeliveryService(DeliveryRepository repository) {
        this.repository = repository;
    }

    public void receiveOrder(Order order) {
        // 1. 주문 유효성 검사 수행 (예: 주소 확인)
        // 2-1. 성공시 배송 요청 내역 저장 (DB)
        // 2-2. 실패시 자세한 오류 내역을 이력으로 저장 (DB)
    }

    public void retrieveShipping() {
        // - shipment id 로 배송 이력을 조회할 수 있다
        // - order id 로 배송 이력을 조회할 수 있다
        // - 시간순으로 배송 이력을 정렬할 수 있다
        // - 상태 기준으로 배송 이력을 조회할 수 있다
    }

    public Delivery prepare(final OrderNo orderNo) {
        Optional<Delivery> delivery = repository.findByOrderNo(orderNo);
        delivery.ifPresent(Delivery::prepare);
        return delivery.orElseThrow(() -> new DeliveryNotFoundException(orderNo));
    }

    public Delivery ship(final OrderNo orderNo) {
        Optional<Delivery> delivery = repository.findByOrderNo(orderNo);
        delivery.ifPresent(Delivery::ship);
        return delivery.orElseThrow(() -> new DeliveryNotFoundException(orderNo));
    }
}
