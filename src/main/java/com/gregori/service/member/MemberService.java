package com.gregori.service.member;

import com.gregori.domain.member.Member;

public interface MemberService {
    Long signup(Member member);
    Member updateMember(Long memberId, Member member);
    String deleteMember(Long memberId);
    Member findMember(Long memberId);
}
