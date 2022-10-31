package kitchenpos.order.repository;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableAndOrderStatusIn(OrderTable orderTable, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableInAndOrderStatusIn(List<OrderTable> orderTables, List<OrderStatus> orderStatuses);
}
