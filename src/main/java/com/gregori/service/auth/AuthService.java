package com.gregori.service.auth;

import org.springframework.http.ResponseEntity;

import com.gregori.dto.auth.AuthSignInDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
	ResponseEntity<String> signIn(AuthSignInDto memberSignInDto, HttpServletRequest request, HttpServletResponse response);
	ResponseEntity<String> signOut(HttpServletRequest request, HttpServletResponse response);
}
