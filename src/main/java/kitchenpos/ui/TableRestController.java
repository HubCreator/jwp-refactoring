package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.OrderTablesResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = tableService.create(orderTableRequest);
        final OrderTableResponse orderTableResponse = OrderTableResponse.of(orderTable);
        return ResponseEntity.created(URI.create("/api/tables/" + orderTable.getId())).body(orderTableResponse);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<OrderTablesResponse> list() {
        final List<OrderTable> orderTables = tableService.list();
        final OrderTablesResponse orderTablesResponse = OrderTablesResponse.from(orderTables);
        return ResponseEntity.ok().body(orderTablesResponse);
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable final Long orderTableId,
                                                          @RequestBody final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = tableService.changeEmpty(orderTableId, orderTableRequest);
        final OrderTableResponse orderTableResponse = OrderTableResponse.of(orderTable);
        return ResponseEntity.ok().body(orderTableResponse);
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(@PathVariable final Long orderTableId,
                                                                   @RequestBody final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = tableService.changeNumberOfGuests(orderTableId, orderTableRequest);
        final OrderTableResponse orderTableResponse = OrderTableResponse.of(orderTable);
        return ResponseEntity.ok().body(orderTableResponse);
    }
}
