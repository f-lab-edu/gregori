package com.gregori.service.member;

import com.gregori.domain.member.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberStore memberStore;

    @Override
    public Member registerMember(Member initMember) {
        Member member = memberStore.create(initMember);

        return member;
    }

    @Override
    public Member updateMember(Long memberId, Member updateMember) {
        Member member = memberStore.update(memberId, updateMember);

        return member;
    }

    @Override
    public String deleteMember(Long memberId) {
        memberStore.delete(memberId);

        return "회원 탈퇴가 완료되었습니다.";
    }

    @Override
    public Member getMemberById(Long memberId) {
        return memberStore.getMemberById(memberId);
    }
}
