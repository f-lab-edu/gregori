package com.gregori.member.domain;

import com.gregori.auth.domain.Authority;
import com.gregori.common.AbstractEntity;
import com.gregori.common.domain.IsDeleted;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.gregori.common.domain.IsDeleted.FALSE;
import static com.gregori.common.domain.IsDeleted.TRUE;

@Getter
@NoArgsConstructor
public class Member extends AbstractEntity {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Authority authority;
    private IsDeleted isDeleted;

    @Builder
    public Member(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.authority = Authority.GENERAL_MEMBER;
        this.isDeleted = FALSE;
    }

    public void sellingMember() {
        this.authority = Authority.SELLING_MEMBER;
    }

    public void adminMember() {
        this.authority = Authority.ADMIN_MEMBER;
    }

    public void isDeletedTrue() {
        this.isDeleted = TRUE;
    }
}
