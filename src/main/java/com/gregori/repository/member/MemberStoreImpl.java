package com.gregori.repository.member;

import com.gregori.domain.member.Member;
import com.gregori.repository.mapper.MemberMapper;
import com.gregori.service.member.MemberStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class MemberStoreImpl implements MemberStore {
    private final MemberMapper memberMapper;

    @Override
    public Long save(Member member) {
        if (!StringUtils.hasText(member.getName())) throw new RuntimeException("Empty name");
        if (!StringUtils.hasText(member.getEmail())) throw new RuntimeException("Empty email");
        if (!StringUtils.hasText(member.getPassword())) throw new RuntimeException("Empty password");

        return memberMapper.insert(member);
    }

    @Override
    public Long update(Long memberId, Member member) {
        if (memberId == null) throw new RuntimeException("Empty id");
        if (!StringUtils.hasText(member.getName())) throw new RuntimeException("Empty name");
        if (!StringUtils.hasText(member.getPassword())) throw new RuntimeException("Empty password");

        Member newMember = findById(memberId);
        newMember.updateMemberInfo(member.getName(), member.getPassword());

        System.out.println("수정: " + member.getStatus());
        System.out.println("기존: " + newMember.getStatus());

        return memberMapper.update(newMember);
    }

    @Override
    public Long deactivate(Long memberId) {
        Member member = findById(memberId);
        member.deactivate();

        return memberMapper.update(member);
    }

    @Override
    public Member findById(Long memberId) {
        return memberMapper.findById(memberId);
    }
}
