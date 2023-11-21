CREATE TABLE members (
    id         BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '회원 인덱스',
    name       VARCHAR(255)          NOT NULL             COMMENT '회원 이름',
    email      VARCHAR(255)          NOT NULL             COMMENT '회원 이메일',
    password   VARCHAR(255)          NOT NULL             COMMENT '회원 비밀번호',
    status     VARCHAR(255)          NOT NULL             COMMENT '회원 상태: ACTIVATE-활성화, DEACTIVATE-비활성화',
    created_at DATETIME              NOT NULL             COMMENT '회원 가입 날짜',
    updated_at DATETIME              NOT NULL             COMMENT '회원 수정 날짜'
);


CREATE TABLE items (
    id         BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '상품 인덱스',
    name       VARCHAR(255)          NOT NULL             COMMENT '상품 이름',
    price      BIGINT                NOT NULL             COMMENT '상품 가격',
    inventory  BIGINT                NOT NULL             COMMENT '상품 재고',
    status     VARCHAR(255)          NOT NULL             COMMENT '상품 상태: PRE_SALE-준비중, ON_SALE-판매중, END_OF_SALE-판매종료',
    created_at DATETIME              NOT NULL             COMMENT '상품 가입 날짜',
    updated_at DATETIME              NOT NULL             COMMENT '상품 수정 날짜'
);
