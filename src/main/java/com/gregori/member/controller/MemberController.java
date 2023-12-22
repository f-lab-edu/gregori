package com.gregori.member.controller;

import java.net.URI;

import com.gregori.member.dto.MemberRegisterDto;
import com.gregori.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody @Valid MemberRegisterDto memberRegisterDto) {

        Long memberId = memberService.register(memberRegisterDto);

        return ResponseEntity.created(URI.create("/member/" + memberId)).build();
    }
}
