package com.gregori.service.member;

import com.gregori.domain.member.Member;

public interface MemberStore {
    Member create(Member member);
    Member update(Long memberId, Member member);
    String delete(Long memberId);
    Member getMemberById(Long memberId);
}
