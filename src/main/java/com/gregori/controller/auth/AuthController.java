package com.gregori.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gregori.config.jwt.TokenProvider;
import com.gregori.dto.auth.AuthSignInDto;
import com.gregori.dto.auth.TokenDto;
import com.gregori.dto.auth.TokenRequestDto;
import com.gregori.service.auth.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;
	private final TokenProvider tokenProvider;

	@PostMapping("/signin")
	public ResponseEntity<TokenDto> signIn(@RequestBody @Valid AuthSignInDto authSignInDto) {
		return ResponseEntity.ok(authService.signIn(authSignInDto));
	}

	@PostMapping("/signout")
	public ResponseEntity<String> signOut(@RequestBody TokenRequestDto tokenRequestDto) {
		authService.signOut(tokenRequestDto);
		return ResponseEntity.ok().body("성공적으로 로그아웃했습니다.");
	}
}
