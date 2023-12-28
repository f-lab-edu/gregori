package com.gregori.member.controller;

import java.net.URI;

import com.gregori.common.CookieGenerator;
import com.gregori.auth.domain.Login;
import com.gregori.auth.service.AuthService;
import com.gregori.common.exception.NotFoundException;
import com.gregori.member.domain.SessionMember;
import com.gregori.member.dto.MemberPasswordUpdateDto;

import com.gregori.member.dto.MemberRegisterDto;
import com.gregori.member.dto.MemberResponseDto;
import com.gregori.member.dto.MemberNameUpdateDto;
import com.gregori.member.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.gregori.common.CookieGenerator.COOKIE_NAME;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Validated MemberRegisterDto dto) {

        Long memberId = memberService.register(dto);

        return ResponseEntity.created(URI.create("/member/" + memberId)).build();
    }

    @PostMapping("/name")
    public ResponseEntity<Void> updateMemberName(@Login SessionMember sessionMember,
        @RequestBody @Validated MemberNameUpdateDto dto) {

        memberService.updateMemberName(sessionMember.getId(), dto);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password")
    public ResponseEntity<Void> updateMemberPassword(@Login SessionMember sessionMember,
        @RequestBody @Validated MemberPasswordUpdateDto dto) {

        memberService.updateMemberPassword(sessionMember.getId(), dto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@Login SessionMember sessionMember,
        HttpSession session, @CookieValue(name = COOKIE_NAME) Cookie cookie) {

        if (session == null || cookie == null) {
            throw new NotFoundException("쿠키 혹은 세션을 찾을 수 없습니다.");
        }

        memberService.deleteMember(sessionMember.getId());
        session.invalidate();

        ResponseCookie newCookie = CookieGenerator.createLogoutCookie();

        return ResponseEntity.noContent()
            .header(HttpHeaders.SET_COOKIE, newCookie.toString())
            .build();
    }

    @GetMapping
    public ResponseEntity<MemberResponseDto> getMember(@Login SessionMember sessionMember) {

        MemberResponseDto response = memberService.getMember(sessionMember.getId());

        return ResponseEntity.ok().body(response);
    }
}
