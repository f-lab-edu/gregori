package com.gregori.service.member;

import com.gregori.domain.member.Member;

public interface MemberService {
    Member registerMember(Member member);
    Member updateMember(Long memberId, Member member);
    String deleteMember(Long memberId);
    Member getMemberById(Long memberId);
}
