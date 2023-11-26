package com.gregori.service.auth;

import java.util.Arrays;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.common.exception.NotFoundException;
import com.gregori.domain.auth.Session;
import com.gregori.domain.member.Member;
import com.gregori.dto.auth.AuthSignInDto;
import com.gregori.mapper.MemberMapper;
import com.gregori.mapper.SessionMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionAuthServiceImpl implements AuthService {
	private final MemberMapper memberMapper;
	private final SessionMapper sessionMapper;

	@Override
	@Transactional
	public ResponseEntity<String> signIn(AuthSignInDto memberSignInDto,
		HttpServletRequest request, HttpServletResponse response) {
		Member member = memberMapper.findByEmailAndPassword(memberSignInDto.getEmail(), memberSignInDto.getPassword())
			.orElseThrow(NotFoundException::new);

		String sessionId = UUID.randomUUID().toString();
		sessionMapper.insert(Session.builder()
			.id(sessionId)
			.memberId(member.getId())
			.build());

		ResponseCookie cookie = ResponseCookie.from("seesion-id", sessionId)
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(1800)
			.build();

		return ResponseEntity
			.ok()
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.build();
	}

	@Override
	@Transactional
	public ResponseEntity<String> signOut(HttpServletRequest request, HttpServletResponse response) {
		expire(request);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body("로그아웃이 완료되었습니다.");
	}

	@Transactional
	public Session findSessionById(HttpServletRequest request) {
		Cookie cookie = findCookieByName(request);
		if (cookie.getValue() == null) {
			return null;
		}

		return sessionMapper.findById(cookie.getValue()).orElse(null);
	}

	@Transactional
	public void expire(HttpServletRequest request) {
		Cookie cookie = findCookieByName(request);
		if (cookie != null) {
			sessionMapper.delete(cookie.getValue());
		}
	}

	public Cookie findCookieByName(HttpServletRequest request) {
		if (request.getCookies() == null) {
			return null;
		}

		return Arrays.stream(request.getCookies())
			.filter(cookie -> cookie.getName().equals("session-id"))
			.findAny()
			.orElse(null);
	}
}
