package com.aws.peach.interfaces.api;

import com.aws.peach.application.DeliveryDetail;
import com.aws.peach.application.DeliveryQueryService;
import com.aws.peach.application.DeliveryService;
import com.aws.peach.application.Order;
import com.aws.peach.domain.delivery.DeliveryId;
import com.aws.peach.domain.delivery.OrderNo;
import com.aws.peach.interfaces.common.JsonException;
import com.aws.peach.interfaces.common.JsonUtil;
import com.aws.peach.interfaces.common.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final DeliveryQueryService deliveryQueryService;
    private final JsonUtil jsonUtil;

    public DeliveryController(DeliveryService deliveryService,
                              DeliveryQueryService deliveryQueryService,
                              JsonUtil jsonUtil) {
        this.deliveryService = deliveryService;
        this.deliveryQueryService = deliveryQueryService;
        this.jsonUtil = jsonUtil;
    }

    @PostMapping
    public ResponseEntity<DeliveryResponse> create(@Valid @RequestBody ReceiveDeliveryOrderRequest request,
                                                   BindingResult bindingResult) {
        log.info("POST /delivery {}", serialize(request));
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        Order order = ReceiveDeliveryOrderRequest.newOrder(request);
        DeliveryId id = deliveryService.createDeliveryOrder(order);
        DeliveryResponse response = DeliveryResponse.builder().deliveryId(id.value).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDetail> queryById(@PathVariable String deliveryId) {
        log.info("GET /delivery/{}", deliveryId);
        return deliveryQueryService.getDelivery(new DeliveryId(deliveryId))
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.ok().build());
    }

    @GetMapping
    public ResponseEntity<DeliveryDetail> queryByOrderNo(@RequestParam(name = "orderNo") String orderNo) {
        log.info("GET /delivery?orderNo={}", orderNo);
        return deliveryQueryService.getDelivery(new OrderNo(orderNo))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok().build());
    }

    private String serialize(ReceiveDeliveryOrderRequest request) {
        try {
            return jsonUtil.serialize(request);
        } catch (JsonException e) {
            return request.toString();
        }
    }
}
