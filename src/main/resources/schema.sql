CREATE TABLE members (
    id         BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY                                                       COMMENT '회원 인덱스',
    name       VARCHAR(255)          NOT NULL                                                                   COMMENT '회원 이름',
    email      VARCHAR(255)          NOT NULL                                                                   COMMENT '회원 이메일',
    password   VARCHAR(255)          NOT NULL                                                                   COMMENT '회원 비밀번호',
    status     VARCHAR(255)          NOT NULL                                                                   COMMENT '회원 상태: ACTIVATE-활성화, DEACTIVATE-비활성화',
    created_at TIMESTAMP             NOT NULL             DEFAULT CURRENT_TIMESTAMP                             COMMENT '회원 가입 날짜',
    updated_at TIMESTAMP             NOT NULL             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '회원 수정 날짜'
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
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);

CREATE TABLE order_items (
    id             BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY                                                       COMMENT '주문 상품 인덱스',
    order_id       BIGINT                NOT NULL                                                                   COMMENT '주문 인덱스',
    order_count    BIGINT                NOT NULL                                                                   COMMENT '주문 상품 개수',
    item_id        BIGINT                NOT NULL                                                                   COMMENT '상품 인덱스',
    item_name      VARCHAR(255)          NOT NULL                                                                   COMMENT '주문 상품 이름',
    item_price     BIGINT                NOT NULL                                                                   COMMENT '주문 상품 가격',
    status         VARCHAR(255)          NOT NULL                                                                   COMMENT '주문 상품 상태',
    created_at     TIMESTAMP             NOT NULL             DEFAULT CURRENT_TIMESTAMP                             COMMENT '주문 상품 생성 날짜',
    FOREIGN KEY (order_id) REFERENCES order_items(order_id),
    FOREIGN KEY (item_id) REFERENCES items(item_id)
);
