package com.aws.peach.interfaces.api.model;

import com.aws.peach.application.DeliveryQueryService;
import com.aws.peach.interfaces.support.DtoUtil;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DeliverySearchResponse extends DeliveryResponse {
    private List<Search> searchResult;

    public static DeliverySearchResponse of(DeliveryQueryService.SearchResult result) {
        List<Search> parsedResultList = Objects.requireNonNull(result).getMappedResultList(Search::of);
        return new DeliverySearchResponse(parsedResultList);
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Search {
        private String deliveryId;
        private Order order;
        private List<DeliveryProduct> items;
        private String status;
        private String updatedAt;

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
