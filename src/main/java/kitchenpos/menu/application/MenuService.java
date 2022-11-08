package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuProductRequest;
import kitchenpos.menu.application.dto.MenuProductRequest.Create;
import kitchenpos.menu.application.dto.MenuRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.repository.MenuDao;
import kitchenpos.menu.domain.repository.MenuGroupDao;
import kitchenpos.menu.domain.repository.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.repository.ProductDao;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final MenuProductDao menuProductDao,
            final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        validateMenuGroupExists(request);
        Menu menu = request.toMenu();

        List<Create> menuProducts = request.getMenuProducts();
        validatePrice(menuProducts, menu);

        final Menu savedMenu = menuDao.save(menu);

        final Long menuId = savedMenu.getId();
        List<MenuProduct> savedMenuProducts = menuProducts.stream()
                .map(it -> menuProductDao.save(new MenuProduct(menuId, it.getProductId(), it.getQuantity())))
                .collect(Collectors.toList());
        savedMenu.addMenuProducts(savedMenuProducts);

        return new MenuResponse(savedMenu);
    }

    private void validateMenuGroupExists(MenuRequest request) {
        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("[ERROR] 올바른 메뉴 그룹이 존재하지 않습니다.");
        }
    }

    private void validatePrice(List<MenuProductRequest.Create> requests, Menu menu) {
        BigDecimal sum = requests.stream()
                .map(it -> {
                    final Product product = productDao.findById(it.getProductId())
                            .orElseThrow(IllegalArgumentException::new);
                    return product.multiplyPrice(it.getQuantity());
                }).reduce(BigDecimal.ZERO, BigDecimal::add);

        if (menu.isBiggerPrice(sum)) {
            throw new IllegalArgumentException("[ERROR] 메뉴의 가격이 상품들의 전체 가격보다 크면 안됩니다.");
        }
    }

    public List<MenuResponse> list() {
        return menuDao.findAll().stream()
                .peek(menu -> menu.addMenuProducts(menuProductDao.findAllByMenuId(menu.getId())))
                .map(MenuResponse::new)
                .collect(Collectors.toList());
    }
}
