package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.ordertable.Emptiness;
import kitchenpos.domain.ordertable.NumberOfGuests;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.ui.dto.ChangeEmptyRequest;
import kitchenpos.ui.dto.NumberOfGuestsRequest;
import kitchenpos.ui.dto.OrderTableRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @InjectMocks
    private TableService tableService;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("테이블에 대한 주문을 생성한다.")
    void create() {
        // given
        final OrderTableRequest request = new OrderTableRequest(3, false);
        final OrderTable orderTable = new OrderTable(new NumberOfGuests(request.getNumberOfGuests()), Emptiness.from(request.isEmpty()));
        given(orderTableDao.save(any())).willReturn(orderTable);

        // when
        final OrderTable result = tableService.create(request);

        // then
        assertSoftly(softly -> {
            verify(orderTableDao, times(1)).save(any());
            assertThat(result).usingRecursiveComparison().isEqualTo(orderTable);
        });
    }

    @Test
    @DisplayName("테이블에 대한 주문을 조회한다.")
    void list() {
        // given
        final List<OrderTable> orderTables = List.of(
                new OrderTable(new NumberOfGuests(2), Emptiness.NOT_EMPTY),
                new OrderTable(new NumberOfGuests(3), Emptiness.NOT_EMPTY)
        );
        given(orderTableDao.findAll()).willReturn(orderTables);

        // when
        final List<OrderTable> result = tableService.list();

        // then
        assertSoftly(softly -> {
            verify(orderTableDao, times(1)).findAll();
            assertThat(result).usingRecursiveComparison().isEqualTo(orderTables);
        });
    }

    @Nested
    class ChangeEmptyTest {
        @Test
        @DisplayName("요청한 주문 테이블을 찾지 못하면 예외가 발생한다.")
        void cannotFindOrderTable() {
            // given
            given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("이미 테이블이 다른 테이블 그룹에 속해있다면 예외가 발생한다.")
        void emptyTableGroupId() {
            // given
            final OrderTable orderTable = mock(OrderTable.class);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
            given(orderTable.getTableGroupId()).willReturn(3L);

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("해당 테이블이 COOKING, MEAL 상태인데, 테이블의 상태를 변경하려고 하면 예외가 발생한다.")
        void existsByOrderTableIdAndOrderStatusIn() {
            // given
            final OrderTable orderTable = mock(OrderTable.class);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
            given(orderTable.getTableGroupId()).willReturn(null);
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any())).willReturn(true);

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("테이블의 상태를 수정한다.")
        void create() {
            // given
            final OrderTable orderTable = new OrderTable(new NumberOfGuests(3), Emptiness.NOT_EMPTY);

            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any())).willReturn(false);

            // when
            tableService.changeEmpty(anyLong(), new ChangeEmptyRequest(true));

            // then
            assertSoftly(softly -> {
                verify(orderTableDao, times(1)).save(any());
                assertThat(orderTable.isEmpty()).isTrue();
            });
        }
    }

    @Nested
    class ChangeNumberOfGuestsTest {
        @DisplayName("한 테이블의 손님 수가 0보다 작으면 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(ints = {-1, -10, 0})
        void numberOfGuestsNegative(int value) {
            // given
            final NumberOfGuestsRequest request = new NumberOfGuestsRequest(value);

            // when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블을 찾지 못하면 예외가 발생한다.")
        void cannotFindOrderTable() {
            // given
            final NumberOfGuestsRequest request = mock(NumberOfGuestsRequest.class);
            given(request.getNumberOfGuests()).willReturn(3);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, request))
                    .isInstanceOf(IllegalArgumentException.class);

        }

        @Test
        @DisplayName("주문 테이블이 비어있다면 예외가 발생한다.")
        void orderTableAlreadyEmpty() {
            // given
            final NumberOfGuestsRequest request = mock(NumberOfGuestsRequest.class);
            final OrderTable orderTable = mock(OrderTable.class);
            given(request.getNumberOfGuests()).willReturn(3);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
            given(orderTable.isEmpty()).willReturn(true);

            // when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("테이블의 손님 숫자를 변경한다.")
        void changeNumberOfGuests() {
            // given
            final NumberOfGuestsRequest request = new NumberOfGuestsRequest(10);
            final OrderTable orderTable = new OrderTable(new NumberOfGuests(5), Emptiness.NOT_EMPTY);
            final OrderTable savedOrderTable = new OrderTable(new NumberOfGuests(10), Emptiness.NOT_EMPTY);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
            given(orderTableDao.save(any())).willReturn(savedOrderTable);

            // when
            final OrderTable result = tableService.changeNumberOfGuests(1L, request);

            // then
            assertSoftly(softly -> {
                verify(orderTableDao, times(1)).save(any());
                assertThat(result.getCount()).isEqualTo(savedOrderTable.getCount());
            });
        }
    }
}
