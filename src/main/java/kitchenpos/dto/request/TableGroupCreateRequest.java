package kitchenpos.dto.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;

public class TableGroupCreateRequest {

    private List<TableIdRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<TableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableIdRequest> getOrderTables() {
        return orderTables;
    }

    public TableGroup toEntity(List<OrderTable> orderTables, int actualTablesSize) {
        return TableGroup.create(orderTables, actualTablesSize);
    }

    public List<Long> toTableRequestIds() {
        return orderTables.stream()
                .map(TableIdRequest::getId)
                .collect(Collectors.toList());
    }
}
