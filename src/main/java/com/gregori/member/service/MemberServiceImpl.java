package com.gregori.member.service;

import com.gregori.member.dto.MemberSignUpDto;
import com.gregori.member.domain.Member;
import com.gregori.member.dto.MemberResponseDto;
import com.gregori.member.dto.MemberUpdateDto;
import com.gregori.member.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public Long signup(MemberSignUpDto memberSignUpDto) {
        memberMapper.findByEmail(memberSignUpDto.getEmail())
            .ifPresent(m -> {
                throw new RuntimeException("The email already exists.");
            });

        return memberMapper.insert(new MemberSignUpDto().toEntity(memberSignUpDto));
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
