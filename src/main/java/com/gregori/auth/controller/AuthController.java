package com.gregori.auth.controller;

import static com.gregori.common.response.SuccessMessage.SIGNIN_SUCCESS;
import static com.gregori.common.response.SuccessMessage.SIGNOUT_SUCCESS;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gregori.auth.dto.AuthSignInDto;
import com.gregori.auth.dto.TokenRequestDto;
import com.gregori.auth.service.AuthService;
import com.gregori.common.response.CustomResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signin")
	public ResponseEntity<Object> signIn(@RequestBody @Valid AuthSignInDto authSignInDto) {
		CustomResponse<Object> response = CustomResponse
			.success(authService.signIn(authSignInDto), SIGNIN_SUCCESS);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping("/signout")
	public ResponseEntity<Object> signOut(@RequestBody TokenRequestDto tokenRequestDto) {
		CustomResponse<Object> response = CustomResponse
			.success(authService.signOut(tokenRequestDto), SIGNOUT_SUCCESS);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
