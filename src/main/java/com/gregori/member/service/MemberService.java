package com.gregori.member.service;

import com.gregori.member.dto.MemberRegisterDto;
import com.gregori.member.dto.MemberResponseDto;
import com.gregori.member.dto.MemberUpdateDto;

public interface MemberService {
    MemberResponseDto register(MemberRegisterDto memberRegisterDto);
    Long updateMember(MemberUpdateDto mypageUpdateDto);
    Long deactivateMember(Long memberId);
    MemberResponseDto findMemberById(Long memberId);
}
