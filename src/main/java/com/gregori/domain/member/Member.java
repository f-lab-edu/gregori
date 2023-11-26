package com.gregori.domain.member;

import com.gregori.domain.AbstractEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor
public class Member extends AbstractEntity {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Status status;

    @Getter
    @RequiredArgsConstructor
    public enum Status {
        ACTIVATE("활성화"), DEACTIVATE("비활성화");
        private final String description;
    }

    @Builder
    public Member(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.status = Status.ACTIVATE;
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
}
