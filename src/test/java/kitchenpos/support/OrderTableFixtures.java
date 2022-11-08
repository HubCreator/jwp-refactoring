package kitchenpos.support;

import java.util.Arrays;
import java.util.List;
import kitchenpos.table.domain.OrderTable;

public enum OrderTableFixtures {

    ORDER_TABLE1(1L, 0, true),
    ORDER_TABLE2(2L, 0, true),
    ORDER_TABLE3(3L, 0, true),
    ORDER_TABLE4(4L, 0, true),
    ORDER_TABLE5(5L, 0, true),
    ORDER_TABLE6(6L, 0, true),
    ORDER_TABLE7(7L, 0, true),
    ORDER_TABLE8(8L, 0, true),
    ORDER_TABLE_NOT_EMPTY(9L, 0, false);

    private final Long id;
    private final int numberOfGuests;
    private final boolean empty;

    OrderTableFixtures(final Long id, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static List<OrderTable> createAll() {
        return Arrays.asList(ORDER_TABLE1.create(), ORDER_TABLE2.create(), ORDER_TABLE3.create(), ORDER_TABLE4.create(),
                ORDER_TABLE5.create(), ORDER_TABLE6.create(), ORDER_TABLE7.create(), ORDER_TABLE8.create());
    }

    public OrderTable create() {
        return new OrderTable(numberOfGuests, empty);
    }

    public OrderTable createWithIdNull() {
        return new OrderTable(numberOfGuests, empty);
    }
}
