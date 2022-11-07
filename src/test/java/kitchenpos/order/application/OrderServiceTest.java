package kitchenpos.order.application;

import static kitchenpos.support.fixture.OrderTableFixture.createEmptyStatusTable;
import static kitchenpos.support.fixture.OrderTableFixture.createNonEmptyStatusTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.support.IntegrationTest;
import kitchenpos.table.application.OrderValidatorImpl;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends IntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderValidatorImpl orderValidator;

    private OrderTable nonEmptyOrderTable;
    private OrderLineItem orderLineItem;

    @BeforeEach
    void setupFixture() {
        nonEmptyOrderTable = orderTableDao.save(createNonEmptyStatusTable());
        orderLineItem = new OrderLineItem(new OrderMenu(menu.getId(), menu.getName(), menu.getPrice()), 1L);
        final Order order = Order.newOrder(nonEmptyOrderTable.getId(), List.of(orderLineItem), orderValidator);
    }

    @DisplayName("주문 생성 기능")
    @Nested
    class CreateTest {

        @DisplayName("정상 작동")
        @Test
        void create() {
            final OrderRequest orderRequest = new OrderRequest(nonEmptyOrderTable.getId(),
                    List.of(new OrderLineItemRequest(orderLineItem.getOrderMenu().getMenuId(), orderLineItem.getQuantity())));

            final OrderResponse orderResponse = orderService.create(orderRequest);

            final Optional<Order> savedOrder = orderDao.findById(orderResponse.getId());
            assertThat(savedOrder).isPresent();
        }

        @DisplayName("주문메뉴가 존재하지 않는 요청일 경우 예외가 발생한다.")
        @Test
        void create_Exception_NonExistOrderLineItem() {
            final OrderRequest orderRequest = new OrderRequest(nonEmptyOrderTable.getId(), Collections.emptyList());

            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문메뉴가 존재하지 않습니다.");
        }

        @DisplayName("주문받은 메뉴가 실제 저장되어 있는 메뉴에 속하지 않는다면 예외가 발생한다.")
        @Test
        void create_Exception_NotExistMenu() {
            final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(Long.MAX_VALUE, 1L);
            final OrderRequest orderRequest = new OrderRequest(nonEmptyOrderTable.getId(), List.of(orderLineItemRequest));

            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문받은 메뉴가 실제 저장되어 있는 메뉴에 속하지 않습니다.");
        }

        @DisplayName("주문테이블이 존재하지 않으면 예외가 발생한다.")
        @Test
        void create_Exception_NonExistsOrderTable() {
            final OrderRequest orderRequest = new OrderRequest(Long.MAX_VALUE,
                    List.of(new OrderLineItemRequest(orderLineItem.getOrderMenu().getMenuId(), orderLineItem.getQuantity())));

            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문테이블이 존재하지 않습니다.");
        }

        @DisplayName("주문테이블이 empty 상태면 예외가 발생한다.")
        @Test
        void create_Exception_EmptyOrderTable() {
            final Long emptyOrderTableId = orderTableDao.save(createEmptyStatusTable()).getId();
            final OrderRequest orderRequest = new OrderRequest(emptyOrderTableId,
                    List.of(new OrderLineItemRequest(orderLineItem.getOrderMenu().getMenuId(), orderLineItem.getQuantity())));

            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문테이블이 주문을 받을수 없는 상태입니다.");
        }
    }

    @DisplayName("주문상태를 변경하는 기능")
    @Nested
    class ChangeOrderStatusTest {

        @DisplayName("저장된 주문상태가 계산완료이면 예외가 발생한다.")
        @Test
        void changeOrderStatus_Exception_CompletionStatus() {
            final OrderLineItem orderLineItem = new OrderLineItem(new OrderMenu(menu.getId(), menu.getName(), menu.getPrice()), 1L);
            final Order order = new Order(nonEmptyOrderTable.getId(), OrderStatus.COMPLETION, LocalDateTime.now(),
                    List.of(orderLineItem));
            final Long savedOrderId = orderDao.save(order).getId();
            final ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest(
                    OrderStatus.MEAL.name());

            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrderId, changeOrderStatusRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문상태가 이미 식사완료면 상태를 바꾸지 못합니다.");
        }

        @DisplayName("상태를 변경하려는 주문이 저장되어 있지 않으면 예외가 발생한다.")
        @Test
        void changeOrderStatus_Exception_NonExistOrder() {
            final ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest(
                    OrderStatus.MEAL.name());

            assertThatThrownBy(() -> orderService.changeOrderStatus(1000L, changeOrderStatusRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("저장되어 있지 않은 주문입니다.");
        }
    }
}
