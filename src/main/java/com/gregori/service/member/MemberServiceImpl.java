package com.gregori.service.member;

import com.gregori.dto.member.MemberSignUpDto;
import com.gregori.domain.member.Member;
import com.gregori.dto.member.MemberResponseDto;
import com.gregori.dto.member.MemberUpdateDto;
import com.gregori.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

        return memberMapper.insert(Member.builder()
            .name(memberSignUpDto.getName())
            .email(memberSignUpDto.getEmail())
            .password(memberSignUpDto.getPassword())
            .build());
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

        return MemberResponseDto.builder()
            .id(member.getId())
            .name(member.getName())
            .email(member.getEmail())
            .status(member.getStatus().toString())
            .createdAt(member.getCreatedAt())
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponseDto findMemberByEmail(String memberEmail) {
        Member member = memberMapper.findByEmail(memberEmail)
            .orElseThrow(() -> new RuntimeException("Member entity not found by email"));

        return MemberResponseDto.builder()
            .id(member.getId())
            .name(member.getName())
            .email(member.getEmail())
            .status(member.getStatus().toString())
            .createdAt(member.getCreatedAt())
            .build();
    }
}
