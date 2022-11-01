package kitchenpos.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.ui.MenuGroupRestController;
import kitchenpos.ui.MenuRestController;
import kitchenpos.ui.OrderRestController;
import kitchenpos.ui.ProductRestController;
import kitchenpos.ui.TableGroupRestController;
import kitchenpos.ui.TableRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        MenuGroupRestController.class,
        ProductRestController.class,
        TableRestController.class,
        TableGroupRestController.class,
        MenuRestController.class,
        OrderRestController.class
})
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected MenuGroupService menuGroupService;

    @MockBean
    protected MenuService menuService;

    @MockBean
    protected ProductService productService;

    @MockBean
    protected TableService tableService;

    @MockBean
    protected TableGroupService tableGroupService;

    @MockBean
    protected OrderService orderService;
}
