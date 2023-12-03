DROP TABLE IF EXISTS members;
DROP TABLE IF EXISTS refresh_tokens;
DROP TABLE IF EXISTS items;

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
