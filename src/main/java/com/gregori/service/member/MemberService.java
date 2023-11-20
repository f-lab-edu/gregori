package com.gregori.service.member;

import com.gregori.dto.member.MemberSignUpDto;
import com.gregori.domain.member.Member;

public interface MemberService {
    Long signup(MemberSignUpDto memberSignUpDto);
    Long updateMember(Long memberId, Member member);
    Long deleteMember(Long memberId);
    Member findMemberById(Long memberId);
    Member findMemberByEmail(String memberEmail);
}
