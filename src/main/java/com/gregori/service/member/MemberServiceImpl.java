package com.gregori.service.member;

import com.gregori.domain.member.Member;
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
            .orElseThrow(() -> new RuntimeException("Member entity not found"));
    }
}
