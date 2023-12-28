DROP TABLE IF EXISTS order_details CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS sellers CASCADE;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS members;

CREATE TABLE members (
     id         BIGINT       AUTO_INCREMENT PRIMARY KEY NOT NULL                            COMMENT '회원 인덱스',
     name       VARCHAR(255) NOT NULL                                                       COMMENT '회원 이름',
     email      VARCHAR(255) NOT NULL                                                       COMMENT '회원 이메일',
     password   VARCHAR(255) NOT NULL                                                       COMMENT '회원 비밀번호',
     authority  VARCHAR(255) NOT NULL                                                       COMMENT '회원 권한',
     is_deleted VARCHAR(255) NOT NULL                                                       COMMENT '회원 삭제 여부',
     created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP                             COMMENT '회원 가입 날짜',
     updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '회원 수정 날짜'
);

CREATE TABLE categories (
    id         BIGINT       AUTO_INCREMENT PRIMARY KEY NOT NULL                            COMMENT '카테고리 인덱스',
    name       VARCHAR(255) NOT NULL                                                       COMMENT '카테고리 이름',
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP                             COMMENT '카테고리 가입 날짜',
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '카테고리 수정 날짜'
);

CREATE TABLE sellers (
     id              BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL                                  COMMENT '셀러 인덱스',
     member_id       BIGINT       NOT NULL                                                       COMMENT '회원 인덱스',
     business_number VARCHAR(255) NOT NULL                                                       COMMENT '셀러 사업자 등록번호',
     business_name   VARCHAR(255) NOT NULL                                                       COMMENT '셀러 상호 이름',
     is_deleted      VARCHAR(255) NOT NULL                                                       COMMENT '셀러 삭제 여부',
     created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP                             COMMENT '셀러 가입 날짜',
     updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '셀러 수정 날짜',
     CONSTRAINT fk_sellers_member_id FOREIGN KEY (member_id) REFERENCES members(id)
);

CREATE TABLE products (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY NOT NULL                            COMMENT '상품 인덱스',
    seller_id   BIGINT       NOT NULL                                                       COMMENT '셀러 인덱스',
    category_id BIGINT       NOT NULL                                                       COMMENT '카테고리 인덱스',
    name        VARCHAR(255) NOT NULL                                                       COMMENT '상품 이름',
    price       BIGINT       NOT NULL                                                       COMMENT '상품 가격',
    inventory   BIGINT       NOT NULL                                                       COMMENT '상품 재고',
    status      VARCHAR(255) NOT NULL                                                       COMMENT '상품 상태',
    is_deleted  VARCHAR(255) NOT NULL                                                       COMMENT '상품 삭제 여부',
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP                             COMMENT '상품 생성 날짜',
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '상품 수정 날짜',
    CONSTRAINT fk_products_seller_id FOREIGN KEY (seller_id) REFERENCES sellers(id),
    CONSTRAINT fk_products_category_id FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE orders (
    id             BIGINT       AUTO_INCREMENT PRIMARY KEY NOT NULL                            COMMENT '주문 인덱스',
    member_id      BIGINT       NOT NULL                                                       COMMENT '회원 인덱스',
    order_number   VARCHAR(255) NOT NULL                                                       COMMENT '주문 번호',
    payment_method VARCHAR(255) NOT NULL                                                       COMMENT '주문 지불 방법',
    payment_amount BIGINT       NOT NULL                                                       COMMENT '주문 지불 금액',
    delivery_cost  BIGINT       NOT NULL                                                       COMMENT '주문 배송비',
    status         VARCHAR(255) NOT NULL                                                       COMMENT '주문 상태',
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP                             COMMENT '주문 접수 날짜',
    updated_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '주문 수정 날짜',
    CONSTRAINT fk_orders_member_id FOREIGN KEY (member_id) REFERENCES members(id)
);

CREATE TABLE order_details (
    id                BIGINT       AUTO_INCREMENT PRIMARY KEY NOT NULL                            COMMENT '주문 상세 인덱스',
    order_id          BIGINT       NOT NULL                                                       COMMENT '주문 인덱스',
    product_id        BIGINT       NOT NULL                                                       COMMENT '상품 인덱스',
    product_seller_id BIGINT       NOT NULL                                                       COMMENT '주문 상품 셀러 인덱스',
    product_name      VARCHAR(255) NOT NULL                                                       COMMENT '주문 상품 이름',
    product_price     BIGINT       NOT NULL                                                       COMMENT '주문 상품 가격',
    product_count     BIGINT       NOT NULL                                                       COMMENT '주문 상품 개수',
    status            VARCHAR(255) NOT NULL                                                       COMMENT '주문 상품 상태',
    created_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP                             COMMENT '주문 상세 생성 날짜',
    updated_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '주문 상세 수정 날짜',
    CONSTRAINT fk_order_details_order_id FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_order_details_product_id FOREIGN KEY (product_id) REFERENCES products(id)
);
