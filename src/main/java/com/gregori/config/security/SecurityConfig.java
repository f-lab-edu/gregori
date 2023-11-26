package com.gregori.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.gregori.config.jwt.JwtSecurityConfig;
import com.gregori.config.jwt.TokenProvider;
import com.gregori.mapper.RefreshTokenMapper;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final String[] allowedURL = { "/", "/member/register", "/auth/**" };
	private final TokenProvider tokenProvider;
	private final RefreshTokenMapper refreshTokenMapper;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)

			.sessionManagement(configurer -> configurer
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(allowedURL).permitAll()
				.requestMatchers("/mypage/**").authenticated()
				.anyRequest().authenticated())

			.apply(new JwtSecurityConfig(tokenProvider, refreshTokenMapper));

		return http.build();
	}
}
