package com.gregori.service.member;

import com.gregori.dto.member.MemberRegisterDto;
import com.gregori.domain.member.Member;
import com.gregori.dto.member.MemberSignInDto;

public interface MemberService {
    Long register(MemberRegisterDto memberRegisterDto);
    Long signIn(MemberSignInDto memberSignInDto);
    Long signOut(Long memberId);
    Long updateMember(Long memberId, Member member);
    Long deleteMember(Long memberId);
    Member findMemberById(Long memberId);
    Member findMemberByEmail(String memberEmail);
}
