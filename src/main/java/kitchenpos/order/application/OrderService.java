package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuDao;
import kitchenpos.order.application.dto.OrderLineItemRequest;
import kitchenpos.order.application.dto.OrderLineItemRequest.Create;
import kitchenpos.order.application.dto.OrderRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.repository.OrderDao;
import kitchenpos.order.domain.repository.OrderLineItemDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableValidator orderTableValidator;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableValidator orderTableValidator
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest.Create request) {
        final List<OrderLineItemRequest.Create> orderLineItems = request.getOrderLineItems();

        final List<Long> menuIds = orderLineItems.stream()
                .map(Create::getMenuId)
                .collect(Collectors.toList());

        validateMenuExists(orderLineItems, menuIds);
        orderTableValidator.checkEmpty(request.getOrderTableId());

        final Order savedOrder = orderDao.save(Order.create(request.getOrderTableId()));
        setOrderItemsInOrder(orderLineItems, savedOrder, savedOrder.getId());

        return new OrderResponse(savedOrder);
    }

    private void validateMenuExists(List<OrderLineItemRequest.Create> orderLineItems, List<Long> menuIds) {
        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private void setOrderItemsInOrder(List<OrderLineItemRequest.Create> orderLineItems, Order savedOrder,
                                      Long orderId) {
        List<OrderLineItem> collect = orderLineItems.stream()
                .map(it -> {
                    Menu menu = menuDao.findById(it.getMenuId()).orElseThrow(IllegalArgumentException::new);
                    return orderLineItemDao.save(
                            new OrderLineItem(orderId, menu.getId(), menu.getName(), menu.getPrice(), it.getQuantity()));
                })
                .collect(Collectors.toList());
        savedOrder.addOrderLineItems(collect);
    }

    public List<OrderResponse> list() {
        return orderDao.findAll().stream()
                .peek(order -> order.addOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId())))
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest.Status request) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        validateCompletionStatus(savedOrder);
        savedOrder.changeOrderStatus(request.getOrderStatus());

        orderDao.save(savedOrder);
        savedOrder.addOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return new OrderResponse(savedOrder);
    }

    private void validateCompletionStatus(Order order) {
        if (order.isCompletionStatus()) {
            throw new IllegalArgumentException("[ERROR] 주문이 완료된 상태에서는 상태를 못 바꿉니다.");
        }
    }
}
