package com.aws.peach.infrastructure.dummy;

import com.aws.peach.domain.delivery.Delivery;
import com.aws.peach.domain.delivery.DeliveryId;
import com.aws.peach.domain.delivery.DeliveryRepository;
import com.aws.peach.domain.delivery.OrderNo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Optional;

@Repository
@Slf4j
public class DeliveryH2Repository implements DeliveryRepository {

    private final ObjectMapper mapper;
    private final InternalRepository repository;

    public DeliveryH2Repository(ObjectMapper mapper, InternalRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public Optional<Delivery> findById(DeliveryId deliveryId) {
        Optional<DeliveryDto> dto = repository.findById(deliveryId.getValue());
        return dto.map(dto1 -> DeliveryDto.newDelivery(dto1, mapper)).or(Optional::empty);
    }

    @Override
    public Optional<Delivery> findByOrderNo(OrderNo orderNo) {
        Optional<DeliveryDto> dto = repository.findByOrderNo(orderNo.getValue());
        return dto.map(dto1 -> DeliveryDto.newDelivery(dto1, mapper)).or(Optional::empty);
    }

    @Override
    public Delivery save(Delivery delivery) {
        if (delivery.getId() == null) {
            delivery.assignNewId();
        }
        DeliveryDto savedDto = repository.save(DeliveryDto.of(delivery, mapper));
        return DeliveryDto.newDelivery(savedDto, mapper);
    }

    @Repository
    public interface InternalRepository extends CrudRepository<DeliveryDto,String> {
        Optional<DeliveryDto> findByOrderNo(String orderNo);
    }

    @Entity
    @Table(name = "delivery")
    @AllArgsConstructor
    @NoArgsConstructor
    private static class DeliveryDto {
        @Id
        private String id;
        private String orderNo;
        @Column(length = 5000)
        private String jsonStr;

        private static DeliveryDto of(Delivery delivery, ObjectMapper mapper) {
            String id = delivery.getId().getValue();
            String orderNo = delivery.getOrderNo().getValue();
            try {
                String jsonStr = mapper.writeValueAsString(delivery);
                return new DeliveryDto(id, orderNo, jsonStr);
            } catch (JsonProcessingException e) {
                log.error("failed to serialize", e);
                throw new RuntimeException("failed to serialize");
            }
        }

        private static Delivery newDelivery(DeliveryDto dto, ObjectMapper mapper) {
            try {
                return mapper.readValue(dto.jsonStr, Delivery.class);
            } catch (JsonProcessingException e) {
                log.error("failed to deserialize", e);
                throw new RuntimeException("failed to deserialize");
            }
        }
    }
}
