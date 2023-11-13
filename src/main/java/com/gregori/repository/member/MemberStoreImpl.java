package com.gregori.repository.member;

import com.gregori.domain.member.Member;
import com.gregori.service.member.MemberStore;
import org.springframework.util.StringUtils;

public class MemberStoreImpl implements MemberStore {
    private final MemberRepository memberRepository;

    @Override
    public Member create(Member member) {
        return null;
    }

    @Override
    public Member update(Long memberId, Member member) {
        return null;
    }

    @Override
    public String delete(Long memberId) {
        return null;
    }

    @Override
    public Member getMemberById(Long memberId) {
        return null;
    }
}
