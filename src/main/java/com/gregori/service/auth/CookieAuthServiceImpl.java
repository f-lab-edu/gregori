package com.gregori.service.auth;


import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.domain.member.Member;
import com.gregori.dto.auth.AuthSignInDto;
import com.gregori.mapper.MemberMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CookieAuthServiceImpl implements AuthService {
	private final MemberMapper memberMapper;

	@Override
	@Transactional
	public ResponseEntity<String> signIn(AuthSignInDto memberSignInDto,
		HttpServletRequest request, HttpServletResponse response) {
		Member member = memberMapper.findByEmailAndPassword(memberSignInDto.getEmail(), memberSignInDto.getPassword())
			.orElseThrow(() -> new RuntimeException("Member entity not found by email and password"));

		ResponseCookie cookie = ResponseCookie.from("member-id", member.getId().toString())
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(86400)
			.build();

		return ResponseEntity
			.ok()
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.build();
	}

	@Override
	public ResponseEntity<String> signOut(HttpServletRequest request, HttpServletResponse response) {
		ResponseCookie cookie = ResponseCookie
			.from("member-id", null)
			.maxAge(0)
			.build();

		return ResponseEntity
			.ok()
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.build();
	}
}
