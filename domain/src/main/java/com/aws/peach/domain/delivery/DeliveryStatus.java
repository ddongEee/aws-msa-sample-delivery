package com.aws.peach.domain.delivery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
@AllArgsConstructor
public class DeliveryStatus {
    private final Type type;
    private final Instant timestamp;

    public DeliveryStatus(Type type) {
        this.type = type;
        this.timestamp = Instant.now();
    }

    public boolean canBePrepared() {
        return this.type == Type.ORDER_RECEIVED;
    }

    public boolean canBePackaged() {
        return this.type == Type.PREPARING;
    }

    public boolean canBeShipped() {
        return this.type == Type.PACKAGING;
    }

    public enum Type {
        ORDER_RECEIVED, PREPARING, PACKAGING, SHIPPED, DELIVERED;
    }
}


