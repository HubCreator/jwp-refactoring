package kitchenpos.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuResponse {

    private Long id;
    private String name;
    private Long price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProducts;

    private MenuResponse() {
    }

    public MenuResponse(final Long id, final String name, final Long price, final Long menuGroupId,
                        final List<MenuProductDto> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(final Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice().longValue(), menu.getMenuGroupId(),
                toDtos(menu.getMenuProducts()));
    }

    private static List<MenuProductDto> toDtos(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductDto::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductDto> getMenuProducts() {
        return menuProducts;
    }
}
