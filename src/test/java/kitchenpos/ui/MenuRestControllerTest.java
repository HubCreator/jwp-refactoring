package kitchenpos.ui;

import static kitchenpos.fixture.MenuFixture.createMenuRequest;
import static kitchenpos.fixture.MenuFixture.createMenuResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import kitchenpos.RestControllerTest;
import kitchenpos.application.MenuService;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest extends RestControllerTest {

    @MockBean
    private MenuService mockMenuService;

    @DisplayName("메뉴 생성 요청을 처리한다.")
    @Test
    void create() throws Exception {
        MenuRequest menuRequest = createMenuRequest();
        MenuResponse menuResponse = MenuResponse.of(menuRequest.toEntity(1L));

        when(mockMenuService.create(any())).thenReturn(menuResponse);
        mockMvc.perform(post("/api/menus")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/menus/" + menuResponse.getId()))
                .andExpect(content().json(objectMapper.writeValueAsString(menuResponse)));
    }

    @DisplayName("메뉴 목록 반환 요청을 처리한다.")
    @Test
    void list() throws Exception {
        List<MenuResponse> expected = Collections.singletonList(createMenuResponse());
        when(mockMenuService.list()).thenReturn(expected);
        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }
}
