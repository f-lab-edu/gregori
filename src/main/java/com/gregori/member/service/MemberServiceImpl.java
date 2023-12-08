package com.gregori.member.service;

import com.gregori.common.exception.DuplicateException;
import com.gregori.common.exception.NotFoundException;
import com.gregori.member.domain.Member;
import com.gregori.member.dto.MemberRegisterDto;
import com.gregori.member.dto.MemberResponseDto;
import com.gregori.member.dto.MemberUpdateDto;
import com.gregori.member.mapper.MemberMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public MemberResponseDto register(@Valid MemberRegisterDto memberRegisterDto) throws DuplicateException {
        memberMapper.findByEmail(memberRegisterDto.getEmail())
            .ifPresent(m -> {
                throw new DuplicateException();
            });

        Member member = memberRegisterDto.toEntity(passwordEncoder);
        memberMapper.insert(member);

        return new MemberResponseDto().toEntity(member);
    }

    @Override
    @Transactional
    public Long updateMember(MemberUpdateDto mypageUpdateDto) throws NotFoundException {
        Member member = memberMapper.findById(mypageUpdateDto.getId())
            .orElseThrow(NotFoundException::new);
        member.updateMemberInfo(mypageUpdateDto.getName(), mypageUpdateDto.getPassword());

        return memberMapper.update(member);
    }

    @Override
    @Transactional
    public Long deleteMember(Long memberId) throws NotFoundException {
        Member member = memberMapper.findById(memberId)
            .orElseThrow(NotFoundException::new);
        member.deactivate();

        return memberMapper.update(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponseDto getMember(Long memberId) throws NotFoundException {
        Member member = memberMapper.findById(memberId)
            .orElseThrow(NotFoundException::new);

        return new MemberResponseDto().toEntity(member);
    }
}
