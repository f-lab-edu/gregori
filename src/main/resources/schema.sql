CREATE TABLE members (
    id         BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '회원 인덱스',
    name       VARCHAR(255)          NOT NULL             COMMENT '회원 이름',
    email      VARCHAR(255)          NOT NULL             COMMENT '회원 이메일',
    password   VARCHAR(255)          NOT NULL             COMMENT '회원 비밀번호',
    status     VARCHAR(255)          NOT NULL             COMMENT '회원 상태: ACTIVATE-활성화, DEACTIVATE-비활성화',
    created_at DATETIME              NOT NULL             COMMENT '회원 가입 날짜',
    updated_at DATETIME              NOT NULL             COMMENT '회원 수정 날짜'
);
