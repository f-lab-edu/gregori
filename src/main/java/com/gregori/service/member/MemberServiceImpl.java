package com.gregori.service.member;

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
    public Long signup(Member member) {
        if (!StringUtils.hasText(member.getName())) {
            throw new RuntimeException("Empty name");
        }
        if (!StringUtils.hasText(member.getEmail())) {
            throw new RuntimeException("Empty email");
        }
        if (!StringUtils.hasText(member.getPassword())) {
            throw new RuntimeException("Empty password");
        }

        return memberMapper.insert(member);
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
            .orElseThrow(() -> new RuntimeException("Member entity not found"));

        return MemberResponseDto.builder()
            .id(member.getId())
            .name(member.getName())
            .email(member.getEmail())
            .status(member.getStatus().toString())
            .createdAt(member.getCreatedAt())
            .build();
    }
}
