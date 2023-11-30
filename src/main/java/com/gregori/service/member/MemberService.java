package com.gregori.service.member;

import com.gregori.dto.member.MemberSignUpDto;
import com.gregori.dto.member.MemberResponseDto;
import com.gregori.dto.member.MemberUpdateDto;

public interface MemberService {
    Long signup(MemberSignUpDto memberSignUpDto);
    Long updateMember(MemberUpdateDto mypageUpdateDto);
    Long deactivateMember(Long memberId);
    MemberResponseDto findMemberById(Long memberId);
    MemberResponseDto findMemberByEmail(String memberEmail);
}
