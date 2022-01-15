package com.aws.peach.infrastructure.dummy;

import com.aws.peach.domain.delivery.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<Delivery> findAll(int pageNo, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNo, pageSize);
        Page<DeliveryDto> page = repository.findAll(pageRequest);
        return page.stream()
                .map(dto -> DeliveryDto.newDelivery(dto, mapper))
                .collect(Collectors.toList());
    }

    @Override
    public List<Delivery> findAllByStatus(DeliveryStatus.Type type, int pageNo, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNo, pageSize);
        Page<DeliveryDto> page = repository.findAllByStatus(type.name(), pageRequest);
        return page.stream()
                .map(dto -> DeliveryDto.newDelivery(dto, mapper))
                .collect(Collectors.toList());
    }

    @Repository
    public interface InternalRepository extends PagingAndSortingRepository<DeliveryDto,String> {
        Optional<DeliveryDto> findByOrderNo(String orderNo);
        Page<DeliveryDto> findAllByStatus(String status, Pageable pageable);
    }

    @Entity
    @Table(name = "delivery")
    @AllArgsConstructor
    @NoArgsConstructor
    private static class DeliveryDto {
        @Id
        private String id;
        private String orderNo;
        private String status;
        @Column(length = 5000)
        private String jsonStr;

        private static DeliveryDto of(Delivery delivery, ObjectMapper mapper) {
            String id = delivery.getId().getValue();
            String orderNo = delivery.getOrderNo().getValue();
            String status = delivery.getStatus().getType().name();
            try {
                String jsonStr = mapper.writeValueAsString(delivery);
                return new DeliveryDto(id, orderNo, status, jsonStr);
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
