package com.gregori.controller.member;

import com.gregori.domain.member.Member;
import com.gregori.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public Long signup(Member initMember) {
        return memberService.signup(initMember);
    }
}
