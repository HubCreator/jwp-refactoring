package kitchenpos.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.TableGroup;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/init_schema.sql")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public abstract class ServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    MenuGroupService menuGroupService;

    @Autowired
    MenuService menuService;

    @Autowired
    TableService tableService;

    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    OrderService orderService;

    @Autowired
    ProductDao productDao;

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    MenuDao menuDao;

    @Autowired
    OrderTableDao orderTableDao;

    @Autowired
    TableGroupDao tableGroupDao;

    @Autowired
    OrderLineItemDao orderLineItemDao;

    @Autowired
    OrderDao orderDao;

    protected MenuProduct 메뉴_상품을_생성한다(String productName, int productPrice, Long quantity) {
        return 메뉴_상품을_생성한다(null, productName, productPrice, quantity);
    }

    protected MenuProduct 메뉴_상품을_생성한다(Long menuId, String productName, int productPrice, Long quantity) {
        Product product = 상품을_저장한다(productName, productPrice);
        return MenuProduct.createWithPrice(menuId, product.getId(), BigDecimal.valueOf(productPrice), quantity);
    }

    protected Product 상품을_저장한다(String name, int price) {
        Product product = new Product(name, BigDecimal.valueOf(price));
        return productDao.save(product);
    }

    protected MenuGroup 메뉴_그룹을_저장한다(String name) {
        MenuGroup menuGroup = new MenuGroup(name);
        return menuGroupDao.save(menuGroup);
    }

    protected Menu 메뉴를_저장한다(String menuName) {
        return 메뉴를_저장한다(menuName, "상품 1", "상품 2");
    }

    protected Menu 메뉴를_저장한다(String menuName, String... menuProductNames) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (String name : menuProductNames) {
            menuProducts.add(메뉴_상품을_생성한다(name, 5000, 1L));
        }

        MenuGroup menuGroup = 메뉴_그룹을_저장한다("메뉴 그룹");

        Menu menu = Menu.create(
                menuName, BigDecimal.valueOf(menuProducts.size() * 5000L), menuGroup.getId(), menuProducts);

        return menuDao.save(menu);
    }

    protected OrderTable 테이블을_저장한다(int numberOfGuests) {
        OrderTable orderTable = new OrderTable(null, numberOfGuests, false);
        return orderTableDao.save(orderTable);
    }

    protected OrderTable 빈_테이블을_저장한다() {
        OrderTable orderTable = new OrderTable(null, 0, true);
        return orderTableDao.save(orderTable);
    }

    protected TableGroup 테이블_그룹을_저장한다(OrderTable... orderTables) {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTables));
        return tableGroupDao.save(tableGroup);
    }

    protected Order 주문을_저장한다() {
        return 주문을_저장한다(테이블을_저장한다(4));
    }

    protected Order 주문을_저장한다(OrderTable orderTable) {
        Menu menu = 메뉴를_저장한다("메뉴");
        OrderLineItem orderLineItem = new OrderLineItem(null, menu.getName(), menu.getPrice(), 3L);
        Order order = Order.newCookingInstanceOf(orderTable.getId(), List.of(orderLineItem));
        Order orderWithLocalDateTime = Order.toOrderWithLocalDateTime(order, LocalDateTime.now());

        Order savedOrder = orderDao.save(orderWithLocalDateTime);
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(
                new OrderLineItem(savedOrder.getId(), orderLineItem.getMenuName(), orderLineItem.getMenuPrice(),
                        orderLineItem.getQuantity()));

        return new Order(savedOrder.getId(), savedOrder.getOrderTableId(), savedOrder.getOrderStatus(),
                savedOrder.getOrderedTime(), List.of(savedOrderLineItem));
    }
}
