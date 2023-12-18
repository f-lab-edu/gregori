package com.gregori.auth.controller;

import static com.gregori.common.response.SuccessMessage.SIGNIN;
import static com.gregori.common.response.SuccessMessage.SIGNOUT;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gregori.auth.dto.AuthSignInDto;
import com.gregori.auth.dto.TokenDto;
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
	public ResponseEntity<CustomResponse<TokenDto>> signIn(@RequestBody @Valid AuthSignInDto authSignInDto) {

		CustomResponse<TokenDto> response = CustomResponse.success(authService.signIn(authSignInDto), SIGNIN);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping("/signout")
	public ResponseEntity<CustomResponse<Long>> signOut(@RequestBody TokenRequestDto tokenRequestDto) {

		CustomResponse<Long> response = CustomResponse.success(authService.signOut(tokenRequestDto), SIGNOUT);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
