package com.gregori.service.member;

import com.gregori.common.exception.DuplicateException;
import com.gregori.common.exception.NotFoundException;
import com.gregori.dto.member.MemberRegisterDto;
import com.gregori.domain.member.Member;
import com.gregori.dto.member.MemberResponseDto;
import com.gregori.dto.member.MemberUpdateDto;
import com.gregori.mapper.MemberMapper;

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
    public MemberResponseDto register(MemberRegisterDto memberRegisterDto) {
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
    public Long updateMember(MemberUpdateDto mypageUpdateDto) {
        Member member = memberMapper.findById(mypageUpdateDto.getId())
            .orElseThrow(NotFoundException::new);
        member.updateMemberInfo(mypageUpdateDto.getName(), mypageUpdateDto.getPassword());

        return memberMapper.update(member);
    }

    @Override
    @Transactional
    public Long deactivateMember(Long memberId) {
        Member member = memberMapper.findById(memberId)
            .orElseThrow(NotFoundException::new);
        member.deactivate();

        return memberMapper.update(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponseDto findMemberById(Long memberId) {
        Member member = memberMapper.findById(memberId)
            .orElseThrow(NotFoundException::new);

        return new MemberResponseDto().toEntity(member);
    }
}
