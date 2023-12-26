package com.gregori.config.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gregori.member.mapper.MemberMapper;
import com.gregori.auth.mapper.RefreshTokenMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	private final TokenProvider tokenProvider;
	private final RefreshTokenMapper refreshTokenMapper;
	private final MemberMapper memberMapper;

	@Override
	public void configure(HttpSecurity builder) throws Exception {

		JwtFilter jwtFilter = new JwtFilter(tokenProvider, refreshTokenMapper, memberMapper);
		builder.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
