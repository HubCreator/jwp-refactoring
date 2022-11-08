package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.stream.Stream;
import kitchenpos.product.application.dto.ProductRequest;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        ProductResponse actucal = productService.create(new ProductRequest("제육", BigDecimal.ONE));

        assertThat(actucal.getId()).isNotNull();
    }

    @DisplayName("상품의 가격이 0이거나 없으면 예외가 발생한다")
    @MethodSource
    @NullSource
    @ParameterizedTest
    void createFailureWhenPriceIsNullOrNegative(BigDecimal price) {
        assertThatThrownBy(() -> productService.create(new ProductRequest("제육 볶음", price)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<BigDecimal> createFailureWhenPriceIsNullOrNegative() {
        return Stream.of(BigDecimal.valueOf(-1L), BigDecimal.valueOf(-100L));
    }

    @DisplayName("상품의 목록을 조회한다.")
    @Test
    void list() {
        productService.create(new ProductRequest("제육", BigDecimal.ONE));

        assertThat(productService.list()).hasSize(1);
    }
}
