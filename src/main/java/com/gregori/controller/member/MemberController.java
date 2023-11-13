package com.gregori.controller.member;

import com.gregori.domain.member.Member;
import com.gregori.service.member.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/create")
    public String registerMember(Member initMember) {
        Member member = memberService.registerMember(initMember);

        return member.getName();
    }

    @PostMapping("/{postId}/update")
    public Long updatePost(Long memberId, Member updateMember) {
        Member member = memberService.updateMember(memberId, updateMember);

        return member.getId();
    }

    @DeleteMapping("/{memberId}/delete")
    public Long deleteMember(Long memberId) {
        memberService.deleteMember(memberId);

        return memberId;
    }

    @GetMapping("{memberId}")
    public Member member(Long memberId) {
        Member member = memberService.getMemberById(memberId);

        return member;
    }
}
