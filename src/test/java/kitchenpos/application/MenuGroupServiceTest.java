package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.menu.application.dto.MenuGroupRequest;
import kitchenpos.menu.application.dto.MenuGroupResponse;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        final String name = "신제품";
        MenuGroupResponse actual = menuGroupService.create(new MenuGroupRequest(name));

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(name)
        );
    }

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    void list() {
        menuGroupService.create(new MenuGroupRequest("신제품"));

        assertThat(menuGroupService.list()).hasSize(1);
    }
}
