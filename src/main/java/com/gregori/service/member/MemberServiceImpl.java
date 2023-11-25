package com.gregori.service.member;

import com.gregori.dto.member.MemberRegisterDto;
import com.gregori.domain.member.Member;
import com.gregori.dto.member.MemberResponseDto;
import com.gregori.dto.member.MemberUpdateDto;
import com.gregori.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public Long register(MemberRegisterDto memberRegisterDto) {
        memberMapper.findByEmail(memberRegisterDto.getEmail())
            .ifPresent(m -> {
                throw new RuntimeException("The email already exists.");
            });

        return memberMapper.insert(new MemberRegisterDto().toEntity(memberRegisterDto));
    }

    @Override
    @Transactional
    public Long updateMember(MemberUpdateDto mypageUpdateDto) {
        Member member = memberMapper.findById(mypageUpdateDto.getId())
            .orElseThrow(() -> new RuntimeException("Member entity not found"));
        member.updateMemberInfo(mypageUpdateDto.getName(), mypageUpdateDto.getPassword());

        return memberMapper.update(member);
    }

    @Override
    @Transactional
    public Long deactivateMember(Long memberId) {
        Member member = memberMapper.findById(memberId)
            .orElseThrow(() -> new RuntimeException("Member entity not found"));
        member.deactivate();

        return memberMapper.update(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponseDto findMemberById(Long memberId) {
        Member member = memberMapper.findById(memberId)
            .orElseThrow(() -> new RuntimeException("Member entity not found by id"));

        return new MemberResponseDto().toEntity(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponseDto findMemberByEmail(String memberEmail) {
        Member member = memberMapper.findByEmail(memberEmail)
            .orElseThrow(() -> new RuntimeException("Member entity not found by email"));

        return new MemberResponseDto().toEntity(member);
    }
}
