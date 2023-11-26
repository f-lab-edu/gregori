package com.gregori.config.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gregori.mapper.RefreshTokenMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
	private final TokenProvider tokenProvider;
	private final RefreshTokenMapper refreshTokenMapper;

	@Override
	public void configure(HttpSecurity builder) throws Exception {
		JwtFilter jwtFilter = new JwtFilter(tokenProvider, refreshTokenMapper);
		builder.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
