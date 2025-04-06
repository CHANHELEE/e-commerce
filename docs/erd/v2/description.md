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
  - **통계 도메인**
    - POPULAR_PRODUCTS 


---

V1 -> V2 변경 항목 

1. **POPULAR_PRODUCTS 연관관계 수정** 
   - AS-IS 
     - POPULAR_PRODUCTS 테이블에 product_name 필드를 보유하여 적재 당시 상품명 사용. 
   - TO-BE
     - POPULAR_PRODUCTS <1:1> PRODUCT 연관관계 설정 
   - 변경 사유 
     - 상품명 변경 시 POPULAR_PRODUCTS 테이블의 상품명이 동기화 되지  
     않기 때문에 연관관계 설정.
   - 확장성 
     - 인기 상품 조회 시 PRODUCT 테이블의 부하를 줄이기 위해  
     AS IS 와 같이 설계 하였으나 추후 성능 이슈가 발생 할 경우 캐싱(redis)을 도입 하여 해결 가능.
2. **ORDERS_PRODUCTS**
   - deleted_at 컬럼 추가
   - 주문에 포함 되는 상품 별 삭제를 위해 추가
3. **ORDERS_PRODUCTS**
    - total_price 컬럼 삭제 
      - 결제시 주문의 총가격을 계산 하는 것이 상품 가격 변동에 대처 할 수 있으므로 해당 컬럼 제거
4. **PAYMENTS**
   - origin_total_price, pay_total_price, discount_price 컬럼 추가
   - 지불 정보에 대한 내용을 결제 도메인에서 관리 하기 위해 추가
5. **ORDER_HISTORIES**
   - 주문 정보 변경에 대한 이력 위해 테이블 추가