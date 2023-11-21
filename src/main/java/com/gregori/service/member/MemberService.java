package com.gregori.service.member;

import com.gregori.domain.member.Member;
import com.gregori.dto.member.MemberResponseDto;
import com.gregori.dto.member.MemberUpdateDto;

public interface MemberService {
    Long signup(Member member);
    Long updateMember(MemberUpdateDto mypageUpdateDto);
    Long deactivateMember(Long memberId);
    MemberResponseDto findMemberById(Long memberId);
}
