package com.gregori.controller.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gregori.dto.auth.AuthSignInDto;
import com.gregori.service.auth.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signin")
	public ResponseEntity<String> signIn(@RequestBody @Valid AuthSignInDto authSignInDto) {
		return authService.signIn(authSignInDto);
	}

	@GetMapping("signout/{memberId}")
	public ResponseEntity<String> signOut(@RequestParam Long memberId) {
		return authService.signOut(memberId);
	}
}
