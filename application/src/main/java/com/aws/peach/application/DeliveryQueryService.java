package com.aws.peach.application;

import com.aws.peach.domain.delivery.*;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true, transactionManager = "transactionManager")
public class DeliveryQueryService {

    private final DeliveryRepository repository;

    public DeliveryQueryService(DeliveryRepository repository) {
        this.repository = repository;
    }

    public Optional<Delivery> getDelivery(DeliveryId deliveryId) {
        return this.repository.findById(deliveryId);
    }

    public Optional<Delivery> getDelivery(OrderNo orderNo) {
        return this.repository.findByOrderNo(orderNo);
    }

    public SearchResult search(SearchCondition condition) {
        List<Delivery> deliveries;
        if (condition.getState().isPresent()) {
            DeliveryStatus.Type status = condition.getState().get().type;
            deliveries = this.repository.findAllByStatus(status, condition.pageNo, condition.pageSize);
        } else {
            deliveries = this.repository.findAll(condition.pageNo, condition.pageSize);
        }
        return new SearchResult(deliveries);
    }

    public static class SearchCondition {
        private final int pageNo;
        private final int pageSize;
        private final State state;

        public static boolean isValidState(String state) {
            return State.isValidState(state);
        }

        private static State resolveState(String state) {
            return (state == null || state.isEmpty()) ? null : State.of(state);
        }

        public SearchCondition(int pageNo, int pageSize, String state) {
            this(pageNo, pageSize, resolveState(state));
        }

        private SearchCondition(int pageNo, int pageSize, State state) {
            this.pageNo = pageNo;
            this.pageSize = pageSize;
            this.state = state;
        }

        public int getPageNo() {
            return pageNo;
        }

        public int getPageSize() {
            return pageSize;
        }

        public Optional<State> getState() {
            return Optional.ofNullable(state);
        }

        @Getter(AccessLevel.PRIVATE)
        private enum State {
            PAID("paid", DeliveryStatus.Type.ORDER_RECEIVED),
            PREPARING("preparing", DeliveryStatus.Type.PREPARING),
            PACKAGING("packaging", DeliveryStatus.Type.PACKAGING),
            SHIPPED("shipped", DeliveryStatus.Type.SHIPPED),
            DELIVERED("delivered", DeliveryStatus.Type.DELIVERED);

            private final String val;
            private final DeliveryStatus.Type type;

            State(String val, DeliveryStatus.Type type) {
                this.val = val;
                this.type = type;
            }

            private static final Map<String, State> VAL_TO_STATE;
            static {
                VAL_TO_STATE = Arrays.stream(State.values())
                        .collect(Collectors.toMap(State::getVal, Function.identity()));
            }

            private static State of(final String state) {
                return VAL_TO_STATE.get(state.toLowerCase(Locale.ROOT));
            }

            private static boolean isValidState(final String state) {
                if (state == null) {
                    return false;
                }
                return VAL_TO_STATE.containsKey(state.toLowerCase(Locale.ROOT));
            }
        }
    }

    @Getter(AccessLevel.PRIVATE)
    public static class SearchResult {
        private final List<Delivery> result;

        public SearchResult() {
            this(Collections.emptyList());
        }

        public SearchResult(List<Delivery> result) {
            this.result = result;
        }

        public <T> List<T> getMappedResultList(Function<Delivery, ? extends T> mapper) {
            return getResult().stream()
                    .map(mapper)
                    .collect(Collectors.toList());
        }
    }
}
