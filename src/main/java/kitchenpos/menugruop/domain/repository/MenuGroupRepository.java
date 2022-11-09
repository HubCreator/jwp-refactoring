package kitchenpos.menugruop.domain.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.menugruop.domain.MenuGroup;

public interface MenuGroupRepository {

    MenuGroup save(MenuGroup entity);

    Optional<MenuGroup> findById(Long id);

    List<MenuGroup> findAll();

    boolean existsById(Long id);
}
