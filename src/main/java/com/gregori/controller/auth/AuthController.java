package com.gregori.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gregori.common.response.CustomResponse;
import com.gregori.dto.auth.AuthSignInDto;
import com.gregori.dto.auth.TokenRequestDto;
import com.gregori.service.auth.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signin")
	public ResponseEntity<Object> signIn(@RequestBody @Valid AuthSignInDto authSignInDto) {
		CustomResponse<Object> response = CustomResponse.success(authService.signIn(authSignInDto), "로그인에 성공했습니다.");

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping("/signout")
	public ResponseEntity<Object> signOut(@RequestBody TokenRequestDto tokenRequestDto) {
		CustomResponse<Object> response = CustomResponse.success(authService.signOut(tokenRequestDto), "로그아웃에 성공했습니다.");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
