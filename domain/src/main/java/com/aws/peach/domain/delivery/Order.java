package com.aws.peach.domain.delivery;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class Order {
    private final OrderNo no;
    private final Instant openedAt;
    private final Orderer orderer;

    @Getter
    @Builder
    public static class Orderer {
        private final String id;
        private final String name;
    }
}
