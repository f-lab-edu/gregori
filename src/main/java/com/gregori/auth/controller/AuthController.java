package com.gregori.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gregori.auth.dto.AuthSignInDto;
import com.gregori.common.CookieGenerator;
import com.gregori.member.domain.SessionMember;
import com.gregori.auth.service.AuthService;
import com.gregori.common.exception.NotFoundException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import static com.gregori.common.CookieGenerator.COOKIE_NAME;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signin")
	public ResponseEntity<Void> signIn(@RequestBody @Validated AuthSignInDto dto, HttpSession session) {

		SessionMember response = authService.signIn(dto);
		session.setAttribute("member", response);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/signout")
	public ResponseEntity<Void> signOut(HttpSession session, @CookieValue(name = COOKIE_NAME) Cookie cookie) {

		if (session == null || cookie == null) {
			throw new NotFoundException("쿠키 혹은 세션을 찾을 수 없습니다.");
		}
		session.invalidate();

		ResponseCookie logoutCookie = CookieGenerator.createLogoutCookie();

		return ResponseEntity.noContent()
			.header(HttpHeaders.SET_COOKIE, logoutCookie.toString())
			.build();
	}
}
