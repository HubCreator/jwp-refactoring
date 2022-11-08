package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.support.DatabaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
    }

    @DisplayName("product 가격이 0보다 작은 경우 예외가 발생한다.")
    @Test
    void create_ifPriceIsNegative_throwsException() {
        // given
        final ProductRequest request = new ProductRequest("후라이드", BigDecimal.valueOf(-1000));

        // when, then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("product 가격이 null인 경우 예외가 발생한다.")
    @Test
    void create_ifPriceIsNull_throwsException() {
        // given
        final ProductRequest request = new ProductRequest("후라이드", null);

        // when, then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("product를 생성한다.")
    @Test
    void create() {
        // given
        final ProductRequest request = new ProductRequest("후라이드", BigDecimal.valueOf(20000));

        // when
        final Product savedProduct = productService.create(request);

        // then
        assertThat(savedProduct.getId()).isNotNull();
    }

    @DisplayName("product들을 조회한다.")
    @Test
    void list() {
        // given
        productRepository.save(new Product("name", BigDecimal.valueOf(3000)));

        // when
        final List<Product> products = productService.list();

        // then
        assertThat(products.size()).isEqualTo(1);
    }
}
