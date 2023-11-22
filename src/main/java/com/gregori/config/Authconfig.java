package com.gregori.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gregori.mapper.MemberMapper;
import com.gregori.mapper.SessionMapper;
import com.gregori.service.auth.AuthService;
import com.gregori.service.auth.AuthServiceCookieImpl;
import com.gregori.service.auth.AuthServiceSessionImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class Authconfig {
	private final MemberMapper memberMapper;
	private final SessionMapper sessionMapper;

	@Bean
	public AuthService authService() {
		return new AuthServiceSessionImpl(memberMapper, sessionMapper);
	}
}
