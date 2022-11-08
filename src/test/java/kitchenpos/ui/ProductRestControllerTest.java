package kitchenpos.ui;


import static kitchenpos.support.ProductFixtures.PRODUCT1;
import static kitchenpos.support.ProductFixtures.createAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ProductRestController.class)
@ExtendWith(MockitoExtension.class)
public class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @DisplayName("product를 생성한다.")
    @Test
    void create() throws Exception {
        // given
        final Product product = PRODUCT1.create();
        final ProductRequest request = new ProductRequest(product.getName(), product.getPrice());

        given(productService.create(any(ProductRequest.class))).willReturn(product);

        // when
        final ResultActions resultActions = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

        // then
        resultActions.andExpect(status().isCreated());
    }

    @DisplayName("product들을 조회한다.")
    @Test
    void list() throws Exception {
        // given
        final List<Product> products = createAll();

        given(productService.list()).willReturn(products);

        // when
        final ResultActions resultActions = mockMvc.perform(get("/api/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk());
    }
}
