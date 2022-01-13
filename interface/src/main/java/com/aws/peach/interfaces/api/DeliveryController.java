package com.aws.peach.interfaces.api;

import com.aws.peach.application.DeliveryService;
import com.aws.peach.application.CreateDeliveryInput;
import com.aws.peach.domain.delivery.Delivery;
import com.aws.peach.domain.delivery.DeliveryId;
import com.aws.peach.interfaces.api.dto.DeliveryDetailResponse;
import com.aws.peach.interfaces.api.dto.ReceiveDeliveryOrderRequest;
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
    private final JsonUtil jsonUtil;

    public DeliveryController(DeliveryService deliveryService, JsonUtil jsonUtil) {
        this.deliveryService = deliveryService;
        this.jsonUtil = jsonUtil;
    }

    @PostMapping
    public ResponseEntity<DeliveryDetailResponse> create(@Valid @RequestBody ReceiveDeliveryOrderRequest request,
                                                         BindingResult bindingResult) {
        log.info("POST /delivery {}", serialize(request));
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        // TODO get sender information
        CreateDeliveryInput order = ReceiveDeliveryOrderRequest.newCreateDeliveryInput(request);
        Delivery delivery = deliveryService.createDeliveryOrder(order);
        DeliveryDetailResponse response = DeliveryDetailResponse.of(delivery);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{deliveryId}/prepare")
    public ResponseEntity<DeliveryDetailResponse> prepare(@PathVariable String deliveryId) {
        log.info("PUT /delivery/{}/prepare", deliveryId);
        Delivery delivery = deliveryService.prepare(new DeliveryId(deliveryId));
        DeliveryDetailResponse response = DeliveryDetailResponse.of(delivery);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{deliveryId}/package")
    public ResponseEntity<DeliveryDetailResponse> pack(@PathVariable String deliveryId) {
        log.info("PUT /delivery/{}/package", deliveryId);
        Delivery delivery = deliveryService.pack(new DeliveryId(deliveryId));
        DeliveryDetailResponse response = DeliveryDetailResponse.of(delivery);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{deliveryId}/ship")
    public ResponseEntity<DeliveryDetailResponse> ship(@PathVariable String deliveryId) {
        log.info("PUT /delivery/{}/ship", deliveryId);
        Delivery delivery = deliveryService.ship(new DeliveryId(deliveryId));
        DeliveryDetailResponse response = DeliveryDetailResponse.of(delivery);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{deliveryId}/complete")
    public ResponseEntity<DeliveryDetailResponse> complete(@PathVariable String deliveryId) {
        log.info("PUT /delivery/{}/complete", deliveryId);
        Delivery delivery = deliveryService.complete(new DeliveryId(deliveryId));
        DeliveryDetailResponse response = DeliveryDetailResponse.of(delivery);
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
