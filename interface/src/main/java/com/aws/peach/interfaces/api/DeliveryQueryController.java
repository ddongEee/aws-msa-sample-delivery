package com.aws.peach.interfaces.api;

import com.aws.peach.application.DeliveryQueryService;
import com.aws.peach.domain.delivery.DeliveryId;
import com.aws.peach.domain.delivery.OrderNo;
import com.aws.peach.interfaces.api.dto.DeliveryDetailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/delivery")
public class DeliveryQueryController {
    private final DeliveryQueryService deliveryQueryService;

    public DeliveryQueryController(DeliveryQueryService deliveryQueryService) {
        this.deliveryQueryService = deliveryQueryService;
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDetailResponse> queryById(@PathVariable String deliveryId) {
        log.info("GET /delivery/{}", deliveryId);
        return deliveryQueryService.getDelivery(new DeliveryId(deliveryId))
                .map(DeliveryDetailResponse::of)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok().build());
    }

    @GetMapping
    public ResponseEntity<DeliveryDetailResponse> queryByOrderNo(@RequestParam(name = "orderNo") String orderNo) {
        log.info("GET /delivery?orderNo={}", orderNo);
        return deliveryQueryService.getDelivery(new OrderNo(orderNo))
                .map(DeliveryDetailResponse::of)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok().build());
    }
}
