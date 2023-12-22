package com.gregori.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gregori.auth.dto.AuthSignInDto;
import com.gregori.auth.dto.TokenDto;
import com.gregori.auth.dto.TokenRequestDto;
import com.gregori.auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signin")
	public ResponseEntity<TokenDto> signIn(@RequestBody @Valid AuthSignInDto authSignInDto) {

		TokenDto response = authService.signIn(authSignInDto);

		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/signout")
	public ResponseEntity<Long> signOut(@RequestBody TokenRequestDto tokenRequestDto) {

		authService.signOut(tokenRequestDto);

		return ResponseEntity.ok().build();
	}
}
