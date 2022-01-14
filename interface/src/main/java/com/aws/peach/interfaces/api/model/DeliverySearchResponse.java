package com.aws.peach.interfaces.api.model;

import com.aws.peach.interfaces.support.DtoUtil;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Builder
@Getter
public class DeliverySearchResponse extends DeliveryResponse {
    private List<Search> searchResult;

    public static DeliverySearchResponse of(List<com.aws.peach.domain.delivery.Delivery> result) {
        List<Search> parsedResult = Objects.requireNonNull(result).stream()
                .map(Search::of)
                .collect(Collectors.toList());
        return new DeliverySearchResponse(parsedResult);
    }

    @Builder
    @Getter
    public static class Search {
        private final String deliveryId;
        private final Order order;
        private final List<DeliveryProduct> items;
        private final String status;
        private final String updatedAt;

        public static Search of(com.aws.peach.domain.delivery.Delivery delivery) {
            Order order = Order.of(delivery.getOrder());
            List<DeliveryProduct> items = delivery.getItems().getMappedList(DeliveryProduct::of);
            return Search.builder()
                    .deliveryId(delivery.getId().getValue())
                    .order(order)
                    .items(items)
                    .status(delivery.getStatus().getType().name())
                    .updatedAt(DtoUtil.formatTimestamp(delivery.getStatus().getTimestamp()))
                    .build();
        }
    }
}
