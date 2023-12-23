package com.gregori.member.service;

import com.gregori.common.exception.DuplicateException;
import com.gregori.common.exception.NotFoundException;
import com.gregori.member.dto.MemberRegisterDto;
import com.gregori.member.dto.MemberResponseDto;
import com.gregori.member.dto.MemberPasswordUpdateDto;
import com.gregori.member.dto.MemberNameUpdateDto;

public interface MemberService {

    Long register(MemberRegisterDto dto) throws DuplicateException;
    void updateMemberName(MemberNameUpdateDto dto);
    void updateMemberPassword(MemberPasswordUpdateDto dto);
    void deleteMember(Long memberId) throws NotFoundException;
    MemberResponseDto getMember(Long memberId);
}
