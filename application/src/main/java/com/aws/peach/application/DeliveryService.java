package com.aws.peach.application;

import com.aws.peach.domain.delivery.Order;
import org.springframework.stereotype.Component;

@Component
public class DeliveryService { // 백오피스

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

    public void startShipping() {
        // - 해당 배송 이력의 상태를 업데이트 한다
    }

    public void completeShipping() {
        // - 해당 배송 이력의 상태를 업데이트 한다
    }
}
