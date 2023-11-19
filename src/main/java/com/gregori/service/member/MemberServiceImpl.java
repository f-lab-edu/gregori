package com.gregori.service.member;

import com.gregori.domain.member.Member;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberStore memberStore;

    @Override
    @Transactional
    public Long signup(Member initMember) {
        return memberStore.save(initMember);
    }

    @Override
    @Transactional
    public Long updateMember(Long memberId, Member updateMember) {
        return memberStore.update(memberId, updateMember);
    }

    @Override
    @Transactional
    public Long deleteMember(Long memberId) {
        return memberStore.deactivate(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public Member findMemberById(Long memberId) {
        return memberStore.findById(memberId);
    }
}
