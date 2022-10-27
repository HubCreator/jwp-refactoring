# 키친포스

## 요구 사항

- 상품
  - 상품은 이름과 가격을 갖는다.
  - 상품의 가격은 0보다 커야하며 반드시 값을 가져야 한다.
  - 새로운 상품을 등록할 수 있다.
  - 저장된 상품의 전체 목록을 조회할 수 있다.

- 메뉴
  - 저장된 모든 메뉴(menu) 를 조회할 수 있다.
  - 메뉴의 가격은 0보다 커야하며 반드시 값을 가져야 한다.
  - 메뉴는 반드시 어느 메뉴 그룹에 속해야 한다.
  - 메뉴의 가격이 상품(product)의 금액 총합(가격 * 수량) 보다 크면 안된다.
  - 새로운 메뉴를 등록할 수 있다.
  - 새로운 메뉴 그룹을 생성할 수 있다.
  - 전체 메뉴 그룹을 조회할 수 있다.

- 주문
  - 주문의 전체 정보를 조회할 수 있다.
  - 주문은 현재 주문 상태와 주문된 시간, 주문 메뉴 및 수량을 담고 있다.
  - 주문의 상태는 변경될 수 있다.
    - 주문이 이미 계산 완료 상태이면 변경이 불가능하다.
  - 주문을 할 때 하나 이상의 메뉴를 주문해야한다.
  - 주문한 메뉴 항목 개수와 실제 메뉴의 수가 일치해야한다.
  - 주문 테이블이 비어있으면 안된다.
  - 주문을 등록할 수 있다. (주문을 하면 조리 상태가 된다.)

- 테이블
  - 새로운 주문 테이블을 생성할 수 있다.
  - 전체 주문 테이블 목록을 조회할 수 있다.
  - 주문 테이블을 비어있는 상태로 변경할 수 있다.
    - 테이블의 그룹(id)은 비어 있어야 한다.
    - 테이블이 비어있는 상태가 되려면 주문 테이블이 조리 중이거나 식사중인 상태이면 안된다.
  - 주문 테이블의 손님 수를 변경할 수 있다.
    - 손님의 수는 0보다 작을 수 없다.
    - 주문 테이블의 손님 수를 변경하려면 주문 테이블은 비어있으면 안된다.
  - 새로운 단체 지정(table group)을 생성할 수 있다.
    - 새로운 테이블 그룹의 주문 테이블이 비어있거나 그룹화하려는 주문 테이블이 2개 보다 작을 수는 없다.
    - 단체 지정하려는 개별 주문 테이블이 실제 존재하는 주문 테이블이어야 한다.
    - 테이블이 비어있고 이미 단체 지정되지 않은 경우에만 새롭게 지정할 수 있다.
  - 단체 지정(table group)을 해제할 수 있다.
    - 이미 조리 중이거나 식사중인 테이블이 있으면 해제할 수 없다.

## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |

---

# 1단계 - 테스트를 통한 코드 보호

## 미션 요구사항 1

- [x] `kitchenpos` 패키지의 코드를 보고 키친포스의 요구사항을 `README.md` 에 작성한다.

## 미션 요구사항 2

- [x] 정리한 키친포스의 요구사항을 토대로 테스트 코드를 작성한다.
- [x] 모든 Business Object에 대한 테스트 코드를 작성한다.
- [x] `@SpringBootTest` 를 이용한 통합 테스트 코드 또는 `@ExtendWith(MockitoExtension.class)` 를 이용한 단위 테스트 코드를 작성한다.

---

## 프로그래밍 요구사항

Lombok 은 그 강력한 기능만큼 사용상 주의를 요한다.

- 무분별한 setter 메소드 사용
- 객체 간에 상호 참조하는 경우 무한 루프에 빠질 가능성
- [Lombok 사용상 주의점(Pitfall)](https://kwonnam.pe.kr/wiki/java/lombok/pitfall)

이번 미션에서는 Lombok 없이 미션을 진행한다.
