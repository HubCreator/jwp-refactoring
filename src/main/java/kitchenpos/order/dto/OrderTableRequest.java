package kitchenpos.order.dto;

public class OrderTableRequest {
    private final Long id;
    public OrderTableRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
