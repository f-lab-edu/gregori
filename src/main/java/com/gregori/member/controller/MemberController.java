package com.gregori.member.controller;

import static com.gregori.common.response.SuccessMessage.REGISTER;

import com.gregori.common.response.CustomResponse;
import com.gregori.member.dto.MemberRegisterDto;
import com.gregori.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
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
    public ResponseEntity<CustomResponse<Long>> register(@RequestBody @Valid MemberRegisterDto memberRegisterDto) {

        CustomResponse<Long> response = CustomResponse.success(memberService.register(memberRegisterDto), REGISTER);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
