package com.gregori.repository.member;

import com.gregori.domain.member.Member;
import com.gregori.service.member.MemberStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberStoreImpl implements MemberStore {
    private final MemberRepository memberRepository;

    @Override
    public Member save(Member member) {
        if (!StringUtils.hasText(member.getName())) throw new RuntimeException("Empty name");
        if (!StringUtils.hasText(member.getEmail())) throw new RuntimeException("Empty email");
        if (!StringUtils.hasText(member.getPassword())) throw new RuntimeException("Empty password");

        return memberRepository.save(member);
    }

    @Override
    public Member update(Long memberId, Member member) {
        if (memberId == null) throw new RuntimeException("Empty id");
        if (!StringUtils.hasText(member.getName())) throw new RuntimeException("Empty name");
        if (!StringUtils.hasText(member.getEmail())) throw new RuntimeException("Empty email");
        if (!StringUtils.hasText(member.getPassword())) throw new RuntimeException("Empty password");

        Member newMember = findById(memberId);
        newMember.updateMemberInfo(member.getName(), member.getPassword());

        return newMember;
    }

    @Override
    public Member deactivate(Long memberId) {
        Member member = findById(memberId);
        member.deactivate();

        return member;
    }

    @Override
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member entity not found"));
    }
}
