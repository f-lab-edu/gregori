package com.gregori.member.domain;

import com.gregori.auth.domain.Authority;
import com.gregori.common.AbstractEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class Member extends AbstractEntity {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Status status;
    private Authority authority;

    @Getter
    @RequiredArgsConstructor
    public enum Status {

        ACTIVATE("활성화"),
        DEACTIVATE("비활성화");

        private final String description;
    }

    @Builder
    public Member(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.status = Status.ACTIVATE;
        this.authority = Authority.GENERAL_MEMBER;
    }

    public void updateMemberInfo(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public void activate() {
        this.status = Status.ACTIVATE;
    }

    public void deactivate() {
        this.status = Status.DEACTIVATE;
    }

    public void generalMember() {
        this.authority = Authority.GENERAL_MEMBER;
    }

    public void sellingMember() {
        this.authority = Authority.SELLING_MEMBER;
    }
}
