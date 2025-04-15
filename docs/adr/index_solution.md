# ADR-003: 쿠폰 도메인 index 추가

### 문제상황
- 유저(USERS) <1:N> 유저_쿠폰(USERS_COUPONS) <N:1> 쿠폰(COUPONS):  
특정 유저가 중복된 쿠폰을 발급 받을 수 없도록 해야함. 
- user_id 로만 조회할 경우가 많기 때문에  
  user_id 조건으로만 조회 했을 때의 성능 고려

### 해결책
- 유저_쿠폰(USERS_COUPONS) 테이블 : (user_id, coupon_id) UK 설정

### 해결책 상세 내용
- user_id 의 카디널리티가 높고 user_id 로만 조회 될 확률이 높으므로  
  user_id, coupon_id 순으로 설정 


# ADR-004: 주문-상품 도메인 index 추가

### 문제상황
- 1개의 주문에 대해 상품,상품 옵션은 1개만 존재해야함.
- order_id 로만 조회할 경우가 많기 때문에  
order_id 조건으로만 조회 했을 때의 성능 고려 

### 해결책
- 주문(ORDERS) 테이블 : (order_id, product_id, product_option_id) UK 설정

### 해결책 상세 내용
- order_id 의 카디널리티가 높고 order_id 로만 조회 될 확률이 높으므로  
  order_id, product_id, product_option_id 순으로 설정 


# ADR-005: 상품 도메인 index 추가

### 문제상황
- a. 상품(PRODUCTS) <1:1> 인기상품(POPULAR_PRODUCTS) :  1:1 관계 유지 필요  
- b. 상품(PRODUCTS) <1:N> 상품옵션(PRODUCTS_OPTIONS) :  상품 당 옵션 규격은 1개만 존재 해야함  
- c. 상품(PRODUCTS) <1:N> 재고(STOCKS) <1:1> 상품옵션(PRODUCTS_OPTIONS):  
상품, 상품 옵션별 재고는 1개만 존재해야 함

### 해결책
- a. 인기상품(POPULAR_PRODUCTS) 테이블 : (product_id) UK 설정  
- b. 상품옵션(PRODUCTS_OPTIONS) 테이블 : (product_id, size) UK 설정  
  - product_id 의 카디널리티가 높고 product_id 로만 조회 될 확률이 높으므로  
    product_id,size 순으로 설정
- c. 재고(STOCKS) 테이블 : (product_option_id, product_id) UK 설정
  - product_option_id 의 카디널리티가 높고 product_option_id 로만 조회 될 확률이 높으므로  
    product_option_id, product_id 순으로 설정

# ADR-006: 포인트 도메인 index 추가

### 문제상황
- 포인트(POINTS) <1:1> 유저(USERS) : 1:1 관계 유지 필요

### 해결책
- 포인트(POINTS) 테이블: user_id 컬럼 UK 설정 

# ADR-007: 주문-결제 도메인 index 추가

### 문제상황
- 결제(PAYMENTS) <1:1> 주문(ORDERS) :  1:1 관계 유지 필요

### 해결책
- 결제(PAYMENTS) 테이블 : (order_id) UK 설정`
