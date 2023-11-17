package com.gregori.service.member;

import com.gregori.domain.member.Member;

public interface MemberStore {
    Member save(Member member);
    Member update(Long memberId, Member member);
    Member deactivate(Long memberId);
    Member findById(Long memberId);
}
