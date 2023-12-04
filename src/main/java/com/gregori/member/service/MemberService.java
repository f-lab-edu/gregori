package com.gregori.member.service;

import com.gregori.member.dto.MemberSignUpDto;
import com.gregori.member.dto.MemberResponseDto;
import com.gregori.member.dto.MemberUpdateDto;

public interface MemberService {
    Long signup(MemberSignUpDto memberSignUpDto);
    Long updateMember(MemberUpdateDto mypageUpdateDto);
    Long deactivateMember(Long memberId);
    MemberResponseDto findMemberById(Long memberId);
    MemberResponseDto findMemberByEmail(String memberEmail);
}
