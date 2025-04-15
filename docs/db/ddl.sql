-- 유저 테이블
CREATE TABLE users
(
    id         BIGINT      NOT NULL PRIMARY KEY COMMENT '아이디',
    name       VARCHAR(10) NOT NULL COMMENT '이름',
    updated_at DATETIME(6) NOT NULL COMMENT '수정일시',
    created_at DATETIME(6) NOT NULL COMMENT '생성일시'
);

-- 포인트 테이블
CREATE TABLE points
(
    id         BIGINT      NOT NULL PRIMARY KEY COMMENT '아이디',
    user_id    BIGINT      NOT NULL COMMENT '사용자 아이디',
    point      INT         NOT NULL COMMENT '포인트',
    created_at DATETIME(6) NOT NULL COMMENT '생성일시',
    updated_at DATETIME(6) NOT NULL COMMENT '수정일시',
    UNIQUE KEY (user_id),
    CONSTRAINT fk_point_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- 포인트이력 테이블
CREATE TABLE points_histories
(
    id         BIGINT      NOT NULL PRIMARY KEY COMMENT '아이디',
    point_id   BIGINT      NOT NULL COMMENT '아이디',
    point      INT         NOT NULL COMMENT '포인트',
    type       VARCHAR(20) NOT NULL COMMENT '충전 타입',
    created_at DATETIME(6) NOT NULL COMMENT '생성일시',
    CONSTRAINT fk_point_history_point FOREIGN KEY (point_id) REFERENCES points (id)
);

-- 쿠폰 테이블
CREATE TABLE coupons
(
    id             BIGINT      NOT NULL PRIMARY KEY COMMENT '아이디',
    amount         INT         NOT NULL COMMENT '수량',
    discount_price INT         NOT NULL COMMENT '할인 금액',
    name           VARCHAR(50) NOT NULL COMMENT '이름',
    created_at     DATETIME(6) NOT NULL COMMENT '생성일시',
    updated_at     DATETIME(6) NOT NULL COMMENT '수정일시'
);

-- 유저_쿠폰 테이블
CREATE TABLE users_coupons
(
    id         BIGINT      NOT NULL PRIMARY KEY COMMENT '아이디',
    coupon_id  BIGINT      NOT NULL COMMENT '쿠폰아이디',
    user_id    BIGINT      NOT NULL COMMENT '사용자아이디',
    created_at DATETIME(6) NOT NULL COMMENT '생성일시',
    updated_at DATETIME(6) NOT NULL COMMENT '수정일시',
    used_at    DATETIME(6) NULL COMMENT '사용일시',
    UNIQUE KEY uk_user_coupon (user_id, coupon_id),
    CONSTRAINT fk_user_coupon_coupon FOREIGN KEY (coupon_id) REFERENCES coupons (id),
    CONSTRAINT fk_user_coupon_user FOREIGN KEY (user_id) REFERENCES users (id)
);


CREATE TABLE products
(
    id         BIGINT      NOT NULL PRIMARY KEY COMMENT '아이디',
    name       VARCHAR(50) NOT NULL COMMENT '상품명',
    price      INT         NOT NULL COMMENT '가격',
    created_at DATETIME(6) NOT NULL COMMENT '생성일시',
    updated_at DATETIME(6) NOT NULL COMMENT '수정일시'
);

CREATE TABLE products_options
(
    id         BIGINT      NOT NULL PRIMARY KEY COMMENT '아이디',
    product_id BIGINT      NOT NULL COMMENT '상품아이디',
    size       VARCHAR(30) NOT NULL COMMENT '규격',
    stock      BIGINT      NOT NULL COMMENT '재고',
    created_at DATETIME(6) NOT NULL COMMENT '생성일시',
    updated_at DATETIME(6) NOT NULL COMMENT '수정일시',
    UNIQUE KEY uk_product_option (product_id, size),
    CONSTRAINT fk_option_product FOREIGN KEY (product_id) REFERENCES products (id)
);


CREATE TABLE product_stock
(
    id                BIGINT      NOT NULL PRIMARY KEY COMMENT '아이디',
    product_id        BIGINT      NOT NULL COMMENT '상품아이디',
    product_option_id BIGINT      NOT NULL COMMENT '상품옵션아이디',
    stock             BIGINT      NOT NULL COMMENT '재고',
    created_at        DATETIME(6) NOT NULL COMMENT '생성일시',
    updated_at        DATETIME(6) NOT NULL COMMENT '수정일시',
    UNIQUE KEY uk_product_stock (product_id, product_option_id),
    CONSTRAINT fk_stock_product FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_stock_product_option FOREIGN KEY (product_option_id) REFERENCES products_options (id)
);


CREATE TABLE popular_product
(
    id         BIGINT      NOT NULL PRIMARY KEY COMMENT '아이디',
    product_id BIGINT      NOT NULL COMMENT '상품아이디',
    name       VARCHAR(50) NOT NULL COMMENT '상품명',
    rank       INT         NOT NULL COMMENT '랭킹',
    created_at DATETIME(6) NOT NULL COMMENT '생성일시',
    UNIQUE KEY uk_popular_product (product_id),
    CONSTRAINT fk_popular_product FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE orders
(
    id             BIGINT      NOT NULL PRIMARY KEY COMMENT '아이디',
    user_id        BIGINT      NOT NULL COMMENT '사용자아이디',
    user_coupon_id BIGINT      NULL COMMENT '사용자_쿠폰_아이디',
    status         VARCHAR(30) NOT NULL COMMENT '주문상태',
    created_at     DATETIME(6) NOT NULL COMMENT '생성일시',
    updated_at     DATETIME(6) NOT NULL COMMENT '수정일시',
    deleted_at     DATETIME(6) NULL COMMENT '삭제일시',
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_order_user_coupon FOREIGN KEY (user_coupon_id) REFERENCES users_coupons (id)
);

CREATE TABLE orders_products
(
    id                BIGINT      NOT NULL PRIMARY KEY COMMENT '아이디',
    order_id          BIGINT      NOT NULL COMMENT '주문아이디',
    product_id        BIGINT      NOT NULL COMMENT '상품아이디',
    product_option_id BIGINT      NOT NULL COMMENT '상품옵션아이디',
    product_price     INT         NOT NULL COMMENT '상품가격',
    amount            INT         NOT NULL COMMENT '수량',
    created_at        DATETIME(6) NOT NULL COMMENT '생성일시',
    updated_at        DATETIME(6) NOT NULL COMMENT '수정일시',
    deleted_at        DATETIME(6) NULL COMMENT '삭제일시',
    UNIQUE KEY uk_order_product (order_id, product_id, product_option_id),
    CONSTRAINT fk_order_product_order FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_order_product_product FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_order_product_option FOREIGN KEY (product_option_id) REFERENCES products_options (id)
);

CREATE TABLE orders_histories
(
    id         BIGINT      NOT NULL PRIMARY KEY COMMENT '아이디',
    order_id   BIGINT      NOT NULL COMMENT '주문아이디',
    status     VARCHAR(30) NOT NULL COMMENT '주문상태',
    created_at DATETIME(6) NOT NULL COMMENT '생성일시',
    CONSTRAINT fk_order_history_order FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE TABLE payment
(
    id                 BIGINT      NOT NULL PRIMARY KEY COMMENT '아이디',
    order_id           BIGINT      NOT NULL COMMENT '주문아이디',
    status             VARCHAR(30) NOT NULL COMMENT '결제상태',
    origin_total_price INT         NOT NULL COMMENT '실제총금액',
    pay_total_price    INT         NOT NULL COMMENT '실결제금액',
    discount_price     INT         NULL COMMENT '할인금액',
    created_at         DATETIME(6) NOT NULL COMMENT '생성일시',
    updated_at         DATETIME(6) NOT NULL COMMENT '수정일시',
    UNIQUE KEY uk_payment_order (order_id),
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE TABLE payment_status_histories
(
    id                 BIGINT      NOT NULL PRIMARY KEY COMMENT '아이디',
    payment_id         BIGINT      NOT NULL COMMENT '결제아이디',
    pay_total_price    INT         NOT NULL COMMENT '실제 결제금액',
    discount_price     INT         NOT NULL COMMENT '할인 금액',
    origin_total_price INT         NOT NULL COMMENT '실제 총금액',
    status             VARCHAR(30) NOT NULL COMMENT '주문상태',
    created_at         DATETIME(6) NOT NULL COMMENT '생성일시',
    CONSTRAINT fk_payment_status_history_payment FOREIGN KEY (payment_id) REFERENCES payment (id)
);