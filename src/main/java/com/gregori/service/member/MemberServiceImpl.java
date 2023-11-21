package com.gregori.service.member;

import com.gregori.dto.member.MemberRegisterDto;
import com.gregori.domain.member.Member;
import com.gregori.dto.member.MemberSignInDto;
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
    public Long register(MemberRegisterDto memberRegisterDto) {
        if (!StringUtils.hasText(memberRegisterDto.getName())) {
            throw new RuntimeException("Empty name");
        }
        if (!StringUtils.hasText(memberRegisterDto.getEmail())) {
            throw new RuntimeException("Empty email");
        }
        if (!StringUtils.hasText(memberRegisterDto.getPassword())) {
            throw new RuntimeException("Empty password");
        }

        memberMapper.findByEmail(memberRegisterDto.getEmail())
            .ifPresent(m -> {
                throw new RuntimeException("The email already exists.");
            });

        return memberMapper.insert(Member.builder()
            .name(memberRegisterDto.getName())
            .email(memberRegisterDto.getEmail())
            .password(memberRegisterDto.getPassword())
            .build());
    }

    @Override
    @Transactional
    public Long signIn(MemberSignInDto memberSignInDto) {
        Member member = memberMapper.findByEmailAndPassword(memberSignInDto.getEmail(), memberSignInDto.getPassword())
            .orElseThrow(() -> new RuntimeException("Member entity not found by email and password"));

        // 로그인 코드 작성

        return null;
    }

    @Override
    public Long signOut(Long memberId) {
        // 로그아웃 코드 작성
        return null;
    }

    @Override
    @Transactional
    public Long updateMember(Long memberId, Member member) {
        if (memberId == null) {
            throw new RuntimeException("Empty id");
        }
        if (!StringUtils.hasText(member.getName())) {
            throw new RuntimeException("Empty name");
        }
        if (!StringUtils.hasText(member.getPassword())) {
            throw new RuntimeException("Empty password");
        }

        Member newMember = findMemberById(memberId);
        newMember.updateMemberInfo(member.getName(), member.getPassword());

        return memberMapper.update(newMember);
    }

    @Override
    @Transactional
    public Long deleteMember(Long memberId) {
        Member member = findMemberById(memberId);
        member.deactivate();

        return memberMapper.update(member);
    }

    @Override
    @Transactional(readOnly = true)
    public Member findMemberById(Long memberId) {
        return memberMapper.findById(memberId)
            .orElseThrow(() -> new RuntimeException("Member entity not found by id"));
    }

    @Override
    public Member findMemberByEmail(String memberEmail) {
        return memberMapper.findByEmail(memberEmail)
            .orElseThrow(() -> new RuntimeException("Member entity not found by email"));
    }
}
