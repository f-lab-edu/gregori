package com.gregori.service.member;

import com.gregori.domain.member.Member;


public interface MemberStore {
    Long save(Member member);
    Long update(Long memberId, Member member);
    Long deactivate(Long memberId);
    Member findById(Long memberId);
}
