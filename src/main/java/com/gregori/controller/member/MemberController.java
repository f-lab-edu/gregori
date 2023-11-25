package com.gregori.controller.member;

import com.gregori.domain.member.Member;
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
}
