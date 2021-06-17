package microfood.orders.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum OrderStatusEnum {
    REQUESTED {
        @Override
        public Set<OrderStatusEnum> getNextStates() {
            return new HashSet<>(Arrays.asList(
                    ACCEPTED, DECLINED, CANCELLED
            ));
        }
    },
    ACCEPTED {
        @Override
        public Set<OrderStatusEnum> getNextStates() {
            return new HashSet<>(Collections.singleton(PREPARED));
        }
    },
    PREPARED {
        @Override
        public Set<OrderStatusEnum> getNextStates() {
            return new HashSet<>(Collections.singletonList(DELIVERING));
        }
    },
    DECLINED {
        @Override
        public Set<OrderStatusEnum> getNextStates() {
            return new HashSet<>();
        }
    },
    CANCELLED {
        @Override
        public Set<OrderStatusEnum> getNextStates() {
            return new HashSet<>();
        }
    },
    DELIVERING {
        @Override
        public Set<OrderStatusEnum> getNextStates() {
            return new HashSet<>(Collections.singletonList(COMPLETED));
        }
    },
    COMPLETED {
        @Override
        public Set<OrderStatusEnum> getNextStates() {
            return new HashSet<>();
        }
    };

    public abstract Set<OrderStatusEnum> getNextStates();
}
