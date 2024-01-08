package com.gregori.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.auth.dto.AuthSignInDto;
import com.gregori.member.domain.SessionMember;
import com.gregori.common.exception.NotFoundException;
import com.gregori.common.exception.ValidationException;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final MemberMapper memberMapper;

	@Transactional
	public SessionMember signIn(AuthSignInDto authSignInDto) {

		Member member = memberMapper.findByEmail(authSignInDto.getEmail()).orElseThrow(NotFoundException::new);
		boolean matches = passwordEncoder.matches(authSignInDto.getPassword(), member.getPassword());
		if (!matches) {
			throw new ValidationException("비밀번호가 일치하지 않습니다.");
		}

		return new SessionMember().toEntity(member);
	}
}
