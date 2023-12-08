package com.gregori.member.service;

import com.gregori.common.exception.DuplicateException;
import com.gregori.common.exception.NotFoundException;
import com.gregori.member.dto.MemberRegisterDto;
import com.gregori.member.dto.MemberResponseDto;
import com.gregori.member.dto.MemberUpdateDto;

public interface MemberService {
    MemberResponseDto register(MemberRegisterDto memberRegisterDto) throws DuplicateException;
    Long updateMember(MemberUpdateDto mypageUpdateDto) throws NotFoundException;
    Long deleteMember(Long memberId) throws NotFoundException;
    MemberResponseDto getMember(Long memberId);
}
