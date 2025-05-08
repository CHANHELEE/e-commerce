create table coupons
(
    id             bigint auto_increment comment '아이디'
        primary key,
    amount         int         not null comment '수량',
    discount_price int         not null comment '할인 금액',
    name           varchar(50) not null comment '이름',
    created_at     datetime(6) not null comment '생성일시',
    updated_at     datetime(6) not null comment '수정일시'
);

create table products
(
    id         bigint auto_increment comment '아이디'
        primary key,
    name       varchar(50) not null comment '상품명',
    price      int         not null comment '가격',
    created_at datetime(6) not null comment '생성일시',
    updated_at datetime(6) not null comment '수정일시'
);

create table daily_popular_products
(
    id               bigint      not null comment '아이디'
        primary key,
    product_id       bigint      not null comment '상품아이디',
    daily_sold_count int         not null comment '일일 판매 수량',
    created_at       datetime(6) not null comment '생성일시',
    constraint uk_daily_popular_product
        unique (product_id),
    constraint fk_daily_popular_product
        foreign key (product_id) references products (id)
);

create table popular_product
(
    id         bigint auto_increment comment '아이디'
        primary key,
    product_id bigint      not null comment '상품아이디',
    name       varchar(50) not null comment '상품명',
    ranking    int         not null comment '랭킹',
    created_at datetime(6) not null comment '생성일시',
    updated_at datetime(6) not null comment '수정일시',
    constraint uk_popular_product
        unique (product_id),
    constraint fk_popular_product
        foreign key (product_id) references products (id)
);

create table products_options
(
    id         bigint auto_increment comment '아이디'
        primary key,
    product_id bigint      not null comment '상품아이디',
    size       varchar(30) not null comment '규격',
    stock      bigint      not null comment '재고',
    created_at datetime(6) not null comment '생성일시',
    updated_at datetime(6) not null comment '수정일시',
    constraint uk_product_option
        unique (product_id, size),
    constraint fk_option_product
        foreign key (product_id) references products (id)
);

create table product_stock
(
    id                bigint auto_increment comment '아이디'
        primary key,
    product_id        bigint      not null comment '상품아이디',
    product_option_id bigint      not null comment '상품옵션아이디',
    stock             bigint      not null comment '재고',
    created_at        datetime(6) not null comment '생성일시',
    updated_at        datetime(6) not null comment '수정일시',
    constraint uk_product_stock
        unique (product_id, product_option_id),
    constraint fk_stock_product
        foreign key (product_id) references products (id),
    constraint fk_stock_product_option
        foreign key (product_option_id) references products_options (id)
);

create table users
(
    id         bigint auto_increment comment '아이디'
        primary key,
    name       varchar(10) not null comment '이름',
    updated_at datetime(6) not null comment '수정일시',
    created_at datetime(6) not null comment '생성일시'
);

create table points
(
    id         bigint auto_increment comment '아이디'
        primary key,
    user_id    bigint      not null comment '사용자 아이디',
    point      int         not null comment '포인트',
    created_at datetime(6) not null comment '생성일시',
    updated_at datetime(6) not null comment '수정일시',
    constraint user_id
        unique (user_id),
    constraint fk_point_user
        foreign key (user_id) references users (id)
);

create table points_histories
(
    id         bigint auto_increment comment '아이디'
        primary key,
    point_id   bigint      not null comment '아이디',
    point      int         not null comment '포인트',
    type       varchar(20) not null comment '충전 타입',
    created_at datetime(6) not null comment '생성일시',
    constraint fk_point_history_point
        foreign key (point_id) references points (id)
);

create table users_coupons
(
    id         bigint auto_increment comment '아이디'
        primary key,
    coupon_id  bigint      not null comment '쿠폰아이디',
    user_id    bigint      not null comment '사용자아이디',
    created_at datetime(6) not null comment '생성일시',
    updated_at datetime(6) not null comment '수정일시',
    used_at    datetime(6) null comment '사용일시',
    constraint uk_user_coupon
        unique (user_id, coupon_id),
    constraint fk_user_coupon_coupon
        foreign key (coupon_id) references coupons (id),
    constraint fk_user_coupon_user
        foreign key (user_id) references users (id)
);

create table orders
(
    id             bigint auto_increment comment '아이디'
        primary key,
    user_id        bigint      not null comment '사용자아이디',
    user_coupon_id bigint      null comment '사용자_쿠폰_아이디',
    status         varchar(30) not null comment '주문상태',
    created_at     datetime(6) not null comment '생성일시',
    updated_at     datetime(6) not null comment '수정일시',
    deleted_at     datetime(6) null comment '삭제일시',
    constraint fk_order_user
        foreign key (user_id) references users (id),
    constraint fk_order_user_coupon
        foreign key (user_coupon_id) references users_coupons (id)
);

create table orders_histories
(
    id         bigint auto_increment comment '아이디'
        primary key,
    order_id   bigint      not null comment '주문아이디',
    status     varchar(30) not null comment '주문상태',
    created_at datetime(6) not null comment '생성일시',
    constraint fk_order_history_order
        foreign key (order_id) references orders (id)
);

create table orders_products
(
    id                bigint auto_increment comment '아이디'
        primary key,
    order_id          bigint      not null comment '주문아이디',
    product_id        bigint      not null comment '상품아이디',
    product_option_id bigint      not null comment '상품옵션아이디',
    product_price     int         not null comment '상품가격',
    amount            int         not null comment '수량',
    created_at        datetime(6) not null comment '생성일시',
    updated_at        datetime(6) not null comment '수정일시',
    deleted_at        datetime(6) null comment '삭제일시',
    constraint uk_order_product
        unique (order_id, product_id, product_option_id),
    constraint fk_order_product_option
        foreign key (product_option_id) references products_options (id),
    constraint fk_order_product_order
        foreign key (order_id) references orders (id),
    constraint fk_order_product_product
        foreign key (product_id) references products (id)
);

create table payment
(
    id                 bigint auto_increment comment '아이디'
        primary key,
    order_id           bigint      not null comment '주문아이디',
    status             varchar(30) not null comment '결제상태',
    origin_total_price int         not null comment '실제총금액',
    pay_total_price    int         not null comment '실결제금액',
    discount_price     int         null comment '할인금액',
    created_at         datetime(6) not null comment '생성일시',
    updated_at         datetime(6) not null comment '수정일시',
    constraint uk_payment_order
        unique (order_id),
    constraint fk_payment_order
        foreign key (order_id) references orders (id)
);

create table payment_histories
(
    id                 bigint auto_increment comment '아이디'
        primary key,
    payment_id         bigint      not null comment '결제아이디',
    pay_total_price    int         not null comment '실제 결제금액',
    discount_price     int         not null comment '할인 금액',
    origin_total_price int         not null comment '실제 총금액',
    status             varchar(30) not null comment '주문상태',
    created_at         datetime(6) not null comment '생성일시',
    constraint fk_payment_status_history_payment
        foreign key (payment_id) references payment (id)
);

