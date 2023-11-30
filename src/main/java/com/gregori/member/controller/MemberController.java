package com.gregori.member.controller;

import com.gregori.common.response.CustomResponse;
import com.gregori.member.dto.MemberRegisterDto;
import com.gregori.member.dto.MemberResponseDto;
import com.gregori.member.service.MemberService;

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
    public ResponseEntity<CustomResponse<MemberResponseDto>> register(@RequestBody @Valid MemberRegisterDto memberRegisterDto) {
        CustomResponse<MemberResponseDto> response = CustomResponse
            .success(memberService.register(memberRegisterDto), "회원 가입에 성공했습니다.");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
