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
        Member member = memberStore.save(initMember);

        return member.getId();
    }

    @Override
    @Transactional
    public Member updateMember(Long memberId, Member updateMember) {
        Member member = memberStore.update(memberId, updateMember);

        return member;
    }

    @Override
    @Transactional
    public String deleteMember(Long memberId) {
        memberStore.deactivate(memberId);

        return "회원 탈퇴가 완료되었습니다.";
    }

    @Override
    @Transactional(readOnly = true)
    public Member findMember(Long memberId) {
        return memberStore.findById(memberId);
    }
}
