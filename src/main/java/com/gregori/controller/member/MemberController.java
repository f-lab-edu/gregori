package com.gregori.controller.member;

import com.gregori.domain.member.Member;
import com.gregori.dto.member.MemberSignInDto;
import com.gregori.dto.member.MemberRegisterDto;
import com.gregori.service.member.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid MemberRegisterDto memberRegisterDto) {
        memberService.register(memberRegisterDto);

        return ResponseEntity.status(HttpStatus.OK).body("회원 가입에 성공했습니다.");
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody @Valid MemberSignInDto memberSignInDto) {
        memberService.signIn(memberSignInDto);

        return ResponseEntity.status(HttpStatus.OK).body("로그인에 성공했습니다.");
    }

    @GetMapping("signout/{memberId}")
    public ResponseEntity<String> signOut(@RequestParam Long memberId) {

        return ResponseEntity.status(HttpStatus.OK).body("로그아웃에 성공했습니다.");
    }

    @PostMapping("/{memberId}")
    public Long updateMember(Long memberId, Member updateMember) {
        return memberService.updateMember(memberId, updateMember);
    }

    @DeleteMapping("/{memberId}")
    public Long deleteMember(Long memberId) {
        return memberService.deleteMember(memberId);
    }

    @GetMapping("{memberId}")
    public Member findMemberById(Long memberId) {
        return memberService.findMemberById(memberId);
    }
}
