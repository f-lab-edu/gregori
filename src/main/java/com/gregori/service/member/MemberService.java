package com.gregori.service.member;

import com.gregori.dto.member.MemberRegisterDto;
import com.gregori.dto.member.MemberResponseDto;
import com.gregori.dto.member.MemberUpdateDto;

public interface MemberService {
    Long register(MemberRegisterDto memberRegisterDto);
    Long updateMember(MemberUpdateDto mypageUpdateDto);
    Long deactivateMember(Long memberId);
    MemberResponseDto findMemberById(Long memberId);
    MemberResponseDto findMemberByEmail(String memberEmail);
}
