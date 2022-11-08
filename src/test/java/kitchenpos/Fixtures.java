package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderTableCreateRequest;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.common.Price;
import org.assertj.core.api.ListAssert;

@SuppressWarnings("NonAsciiCharacters")
public class Fixtures {

    public static MenuGroup 메뉴그룹_한마리메뉴() {
        return new MenuGroup("한마리메뉴");
    }

    public static Product 상품_후라이드() {
        return new Product(null, "후라이드", new Price(16000));
    }

    public static Menu 메뉴_후라이드치킨() {
        return new Menu("후라이드치킨", BigDecimal.valueOf(16000),
                1L, new MenuProducts(List.of(new MenuProduct(1L, 2, new Price(16000)))));
    }

    public static MenuGroupRequest 메뉴그룹요청_한마리메뉴() {
        return new MenuGroupRequest("한마리메뉴");
    }

    public static MenuGroupRequest 메뉴그룹요청_두마리메뉴() {
        return new MenuGroupRequest("두마리메뉴");
    }

    public static ProductRequest 상품요청_후라이드() {
        return new ProductRequest("후라이드", BigDecimal.valueOf(16000));
    }

    public static MenuProductRequest 메뉴상품요청_후라이드() {
        return new MenuProductRequest(1L, 1);
    }

    public static MenuRequest 메뉴요청_후라이드치킨() {
        return new MenuRequest("후라이드치킨", BigDecimal.valueOf(16000), 1L,
                List.of(메뉴상품요청_후라이드()));
    }

    public static MenuRequest 메뉴요청_치킨그룹(MenuProductRequest... menuProducts) {
        return new MenuRequest("후라이드치킨", BigDecimal.valueOf(16000),
                1L,
                Arrays.asList(menuProducts));
    }

    public static OrderTableCreateRequest 빈테이블생성요청() {
        return new OrderTableCreateRequest(0, true);
    }

    public static TableGroupRequest 테이블그룹요청(List<OrderTableRequest> tables) {
        return new TableGroupRequest(tables);
    }

    public static TableGroupRequest 테이블그룹요청_id(Long... tableIds) {
        List<OrderTableRequest> orderTableRequests = Arrays.stream(tableIds)
                .map(OrderTableRequest::new)
                .collect(Collectors.toList());
        return new TableGroupRequest(orderTableRequests);
    }

    public static OrderLineItemRequest 주문아이템요청(long menuId) {
        return new OrderLineItemRequest(menuId, 1L);
    }

    public static OrderLineItemRequest 주문아이템요청_후라이드() {
        return new OrderLineItemRequest(1L, 1L);
    }

    public static OrderRequest 주문요청_테이블1() {
        return new OrderRequest(1L, List.of(주문아이템요청_후라이드()));
    }

    @SafeVarargs
    public static <ELEMENT> void 검증_필드비교_값포함(ListAssert<ELEMENT> assertThat, ELEMENT... values) {
        assertThat.usingRecursiveFieldByFieldElementComparator()
                .contains(values);
    }

    @SafeVarargs
    public static <ELEMENT> void 검증_필드비교_값포함(List<ELEMENT> list, ELEMENT... values) {
        assertThat(list).usingRecursiveFieldByFieldElementComparator()
                .contains(values);
    }

    public static <ELEMENT> void 검증_필드비교_동일_목록(List<ELEMENT> list, List<ELEMENT> values, String... ignore) {
        for (int i = 0; i < list.size(); i++) {
            assertThat(list.get(i)).usingRecursiveComparison()
                    .ignoringFields(ignore)
                    .isEqualTo(values.get(i));
        }
    }
}
