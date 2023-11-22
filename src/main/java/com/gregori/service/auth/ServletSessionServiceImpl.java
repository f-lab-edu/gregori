package com.gregori.service.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.gregori.domain.member.Member;
import com.gregori.dto.auth.AuthSignInDto;
import com.gregori.mapper.MemberMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServletSessionServiceImpl implements AuthService {
	private final MemberMapper memberMapper;

	@Override
	public ResponseEntity<String> signIn(AuthSignInDto memberSignInDto, HttpServletRequest request,
		HttpServletResponse response) {
		Member member = memberMapper.findByEmailAndPassword(memberSignInDto.getEmail(), memberSignInDto.getPassword())
			.orElseThrow(() -> new RuntimeException("Member entity not found by email and password"));

		HttpSession session = request.getSession(true);
		session.setAttribute("session-id", member.getId());
		session.setMaxInactiveInterval(1800);

		return ResponseEntity
			.ok()
			.build();
	}

	@Override
	public ResponseEntity<String> signOut(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		return ResponseEntity
			.status(HttpStatus.OK)
			.body("로그아웃이 완료되었습니다.");
	}
}
