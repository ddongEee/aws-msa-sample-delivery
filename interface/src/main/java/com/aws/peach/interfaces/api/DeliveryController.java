package com.aws.peach.interfaces.api;

import com.aws.peach.application.DeliveryService;
import com.aws.peach.application.Order;
import com.aws.peach.domain.delivery.DeliveryId;
import com.aws.peach.interfaces.common.JsonException;
import com.aws.peach.interfaces.common.JsonUtil;
import com.aws.peach.interfaces.common.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final JsonUtil jsonUtil;

    public DeliveryController(DeliveryService deliveryService, JsonUtil jsonUtil) {
        this.deliveryService = deliveryService;
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

    private String serialize(ReceiveDeliveryOrderRequest request) {
        try {
            return jsonUtil.serialize(request);
        } catch (JsonException e) {
            return request.toString();
        }
    }
}
