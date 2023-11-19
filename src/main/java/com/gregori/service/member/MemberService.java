package com.gregori.service.member;

import com.gregori.domain.member.Member;

public interface MemberService {
    Long signup(Member member);
    Long updateMember(Long memberId, Member member);
    Long deleteMember(Long memberId);
    Member findMemberById(Long memberId);
}
