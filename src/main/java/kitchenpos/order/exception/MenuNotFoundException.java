package kitchenpos.order.exception;

public class MenuNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "메뉴를 찾을 수 없습니다.";

    public MenuNotFoundException() {
        super(MESSAGE);
    }
}
