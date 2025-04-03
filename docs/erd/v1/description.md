## ERD Description

- 테이블 목록
  - **사용자 도메인** 
    - USERS
    - POINT
    - POINT_HISTORIES
  - **쿠폰 도메인**
    - COUPON
    - USERS_COUPONS
  - **주문 도메인**
    - 테이블 목록 
      - ORDERS
        - deleted_at 컬럼: 주문 실패 또는 주문 생성 상태가  
        일정 시간 유지 되면 주문을 삭제 (deleted_at 컬럼)
      - ORDERS_PRODUCTS
        - 상품의 price는 변동 될 수 있기 때문에 ORDERS_PRODUCTS  
        주문 당시 금액을 저장 하기 위해 중복 컬럼 설계
      - PAYMENTS
      - PAYMENTS_HISTORIES 
        - 1개 주문에 대해 N번의 결제 시도가 가능하므로  
        결제 상태와 결제일시를 저장
  - **상품 도메인**
    - PRODUCTS
    - STOCKS
    - PRODUCTS_OPTIONS