package com.gregori.service.member;

import com.gregori.dto.member.MemberRegisterDto;
import com.gregori.domain.member.Member;

public interface MemberService {
    Long register(MemberRegisterDto memberRegisterDto);
    Long updateMember(Long memberId, Member member);
    Long deleteMember(Long memberId);
    Member findMemberById(Long memberId);
    Member findMemberByEmail(String memberEmail);
}
