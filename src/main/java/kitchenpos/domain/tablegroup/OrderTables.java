package kitchenpos.domain.tablegroup;

import kitchenpos.domain.ordertable.Emptiness;
import kitchenpos.domain.ordertable.OrderTable;

import java.util.List;
import java.util.Objects;

public class OrderTables {
    private final List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public int size() {
        return orderTables.size();
    }

    public void updateAll(final Long tableGroupId, final Emptiness empty) {
        orderTables.forEach(orderTable -> {
            orderTable.updateTableGroupId(tableGroupId);
            orderTable.updateEmpty(empty);
        });
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderTables that = (OrderTables) o;
        return Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTables);
    }
}
