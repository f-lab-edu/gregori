DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS refresh_tokens;
DROP TABLE IF EXISTS members;

CREATE TABLE members (
     id         BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY                                                       COMMENT '회원 인덱스',
     name       VARCHAR(255)          NOT NULL                                                                   COMMENT '회원 이름',
     email      VARCHAR(255)          NOT NULL                                                                   COMMENT '회원 이메일',
     password   VARCHAR(255)          NOT NULL                                                                   COMMENT '회원 비밀번호',
     status     VARCHAR(255)          NOT NULL                                                                   COMMENT '회원 상태',
     authority  VARCHAR(255)          NOT NULL                                                                   COMMENT '회원 권한',
     created_at TIMESTAMP             NOT NULL             DEFAULT CURRENT_TIMESTAMP                             COMMENT '회원 가입 날짜',
     updated_at TIMESTAMP             NOT NULL             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '회원 수정 날짜'
);

CREATE TABLE refresh_tokens (
    id                  BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY                                                       COMMENT '토큰 인덱스',
    refresh_token_key   VARCHAR(255)          NOT NULL                                                                   COMMENT '토큰 키',
    refresh_token_value VARCHAR(255)          NOT NULL                                                                   COMMENT '토큰 값',
    created_at          TIMESTAMP             NOT NULL             DEFAULT CURRENT_TIMESTAMP                             COMMENT '토큰 생성 날짜',
    updated_at          TIMESTAMP             NOT NULL             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '토큰 수정 날짜'
);

CREATE TABLE items (
    id         BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY                                                       COMMENT '상품 인덱스',
    name       VARCHAR(255)          NOT NULL                                                                   COMMENT '상품 이름',
    price      BIGINT                NOT NULL                                                                   COMMENT '상품 가격',
    inventory  BIGINT                NOT NULL                                                                   COMMENT '상품 재고',
    status     VARCHAR(255)          NOT NULL                                                                   COMMENT '상품 상태',
    created_at TIMESTAMP             NOT NULL             DEFAULT CURRENT_TIMESTAMP                             COMMENT '상품 생성 날짜',
    updated_at TIMESTAMP             NOT NULL             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '상품 수정 날짜'
);

CREATE TABLE orders (
    id             BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY                                                       COMMENT '주문 인덱스',
    member_id      BIGINT                NOT NULL                                                                   COMMENT '회원 인덱스',
    order_no       VARCHAR(255)          NOT NULL                                                                   COMMENT '주문 번호',
    payment_method VARCHAR(255)          NOT NULL                                                                   COMMENT '주문 지불 방법',
    payment_amount BIGINT                NOT NULL                                                                   COMMENT '주문 지불 금액',
    delivery_cost  BIGINT                NOT NULL                                                                   COMMENT '주문 배송비',
    status         VARCHAR(255)          NOT NULL                                                                   COMMENT '주문 상태',
    created_at     TIMESTAMP             NOT NULL             DEFAULT CURRENT_TIMESTAMP                             COMMENT '주문 접수 날짜',
    updated_at     TIMESTAMP             NOT NULL             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '주문 수정 날짜',
    CONSTRAINT member_id FOREIGN KEY (member_id) REFERENCES members(id)
);

CREATE TABLE order_items (
    id          BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY                                                       COMMENT '주문 상품 인덱스',
    order_id    BIGINT                NOT NULL                                                                   COMMENT '주문 인덱스',
    order_count BIGINT                NOT NULL                                                                   COMMENT '주문 상품 개수',
    item_id     BIGINT                NOT NULL                                                                   COMMENT '상품 인덱스',
    item_name   VARCHAR(255)          NOT NULL                                                                   COMMENT '주문 상품 이름',
    item_price  BIGINT                NOT NULL                                                                   COMMENT '주문 상품 가격',
    status      VARCHAR(255)          NOT NULL                                                                   COMMENT '주문 상품 상태',
    created_at  TIMESTAMP             NOT NULL             DEFAULT CURRENT_TIMESTAMP                             COMMENT '주문 상품 생성 날짜',
    updated_at  TIMESTAMP             NOT NULL             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '주문 상품 수정 날짜',
    CONSTRAINT order_id FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT item_id FOREIGN KEY (item_id) REFERENCES items(id)
);
