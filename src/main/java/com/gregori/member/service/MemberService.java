package com.gregori.member.service;

import com.gregori.common.exception.DuplicateException;
import com.gregori.common.exception.NotFoundException;
import com.gregori.member.dto.MemberRegisterDto;
import com.gregori.member.dto.MemberResponseDto;
import com.gregori.member.dto.MemberUpdateDto;
import com.gregori.member.dto.MemberPasswordUpdateDto;

public interface MemberService {

    Long register(MemberRegisterDto memberRegisterDto) throws DuplicateException;
    Long updateMember(MemberUpdateDto mypageUpdateDto) throws NotFoundException;
    void updateMemberName(Long memberId, String name);
    void updateMemberPassword(MemberPasswordUpdateDto memberPasswordUpdateDto);
    Long deleteMember(Long memberId) throws NotFoundException;
    MemberResponseDto getMember(Long memberId);
}
