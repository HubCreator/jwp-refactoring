package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private final Long id;
    private final Long orderTableId;
    private String orderStatus;
    private final LocalDateTime orderedTime;
    private final OrderLineItems orderLineItems;

    public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
                 OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(Long orderTableId, String orderStatus, LocalDateTime orderedTime,
                 OrderLineItems orderLineItems) {
        this(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public void placeOrderStatus(final String orderStatus) {
        validateNoCompletion();
        this.orderStatus = orderStatus;
    }

    private void validateNoCompletion() {
        if (orderStatus.equals(OrderStatus.COMPLETION.name())) {
            throw new IllegalArgumentException("주문 상태가 COMPLETION일 시 주문 상태를 변경할 수 없다.");
        }
    }

    public void placeOrderId() {
        orderLineItems.placeOrderId(id);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getItems();
    }
}
