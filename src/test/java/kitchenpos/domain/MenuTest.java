package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.common.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuTest {

    @DisplayName("가격은 0 이상이어야 한다.")
    @Test
    void createAndList_invalidPrice() {
        BigDecimal 음수_가격 = BigDecimal.valueOf(-10000);
        MenuProducts menuProducts = new MenuProducts(List.of(new MenuProduct(1L, 1L, 1L, 1, new Price(16000))));

        assertThatThrownBy(() -> new Menu(1L, "후라이드", 음수_가격,
                1L, menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0 이상이어야 한다.");
    }
}
