package com.gregori.service.auth;

import org.springframework.http.ResponseEntity;

import com.gregori.dto.auth.AuthSignInDto;

public interface AuthService {
	ResponseEntity<String> signIn(AuthSignInDto memberSignInDto);
	ResponseEntity<String> signOut(Long memberId);
}
