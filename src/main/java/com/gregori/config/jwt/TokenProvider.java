package com.gregori.config.jwt;


import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.gregori.auth.dto.TokenDto;
import com.gregori.common.exception.NotFoundException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenProvider {

	private static final String AUTHORIZATION_KEY = "auth";
	private static final String BEARER_TYPE = "bearer";
	private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 30;
	private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;
	private final SecretKey key;

	public TokenProvider(@Value("${jwt.secret}") String secret) {

		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public TokenDto generateToken(Authentication authentication) {

		String authorities = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));
		long now = (new Date()).getTime();

		Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRATION_TIME);
		String accessToken = Jwts.builder()
			.subject(authentication.getName())
			.claim(AUTHORIZATION_KEY, authorities)
			.expiration(accessTokenExpiresIn)
			.signWith(key)
			.compact();

		String refreshToken = Jwts.builder()
			.expiration(new Date(now + REFRESH_TOKEN_EXPIRATION_TIME))
			.signWith(key)
			.compact();

		return TokenDto.builder()
			.grantType(BEARER_TYPE)
			.accessToken(accessToken)
			.accessTokenExpiresIn(accessTokenExpiresIn.getTime())
			.refreshToken(refreshToken)
			.build();
	}

	public Authentication getAuthentication(String accessToken) {

		Claims claims = parseClaims(accessToken);
		if (claims.get(AUTHORIZATION_KEY) == null) {
			throw new NotFoundException("권한 정보가 없습니다.");
		}

		Collection<? extends GrantedAuthority> authorities =
			Arrays.stream(claims.get(AUTHORIZATION_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		UserDetails principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}

	public Claims parseClaims(String accessToken) {

		try {
			return Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken).getPayload();
		} catch (ExpiredJwtException e) {
			log.warn("[ExpiredJwtException] http status: {}, message: {} ", HttpStatus.UNAUTHORIZED, e.getMessage());

			return e.getClaims();
		} catch (Exception e) {
			log.warn("[Exception] http status: {}, message: {} ", HttpStatus.UNAUTHORIZED, e.getMessage());

			return null;
		}
	}

	public boolean validateToken(String token) {

		try {
			Jwts.parser().verifyWith(key).build().parseSignedClaims(token);

			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.warn("[SecurityException | MalformedJwtException] http status: {}, message: {} ", HttpStatus.UNAUTHORIZED, e.getMessage());
		} catch (ExpiredJwtException e) {
			log.warn("[ExpiredJwtException] http status: {}, message: {} ", HttpStatus.UNAUTHORIZED, e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.warn("[UnsupportedJwtException] http status: {}, message: {} ", HttpStatus.UNAUTHORIZED, e.getMessage());
		} catch (IllegalArgumentException e) {
			log.warn("[IllegalArgumentException] http status: {}, message: {} ", HttpStatus.UNAUTHORIZED, e.getMessage());
		}
		return false;
	}
}
