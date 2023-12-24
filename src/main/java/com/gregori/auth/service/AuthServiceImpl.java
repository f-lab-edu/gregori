package com.gregori.auth.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.auth.dto.AuthSignInDto;
import com.gregori.auth.dto.TokenDto;
import com.gregori.auth.dto.TokenRequestDto;
import com.gregori.common.exception.UnauthorizedException;
import com.gregori.common.exception.ValidationException;
import com.gregori.config.jwt.TokenProvider;
import com.gregori.refresh_token.domain.RefreshToken;
import com.gregori.refresh_token.mapper.RefreshTokenMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final RefreshTokenMapper refreshTokenMapper;
	private final TokenProvider tokenProvider;
	private final AuthenticationManager authenticationManager;

	@Override
	@Transactional
	public TokenDto signIn(AuthSignInDto authSignInDto) {
		
		UsernamePasswordAuthenticationToken authenticationToken = authSignInDto.toAuthentication();
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		TokenDto tokenDto = tokenProvider.generateToken(authentication);

		refreshTokenMapper.findByRefreshTokenKey(authentication.getName())
			.ifPresent(refreshToken -> refreshTokenMapper.deleteById(refreshToken.getId()));

		refreshTokenMapper.insert(RefreshToken.builder()
			.refreshTokenKey(authentication.getName())
			.refreshTokenValue(tokenDto.getRefreshToken())
			.build());

		return tokenDto;
	}

	@Override
	@Transactional
	public void signOut(TokenRequestDto tokenRequestDto) {

		Authentication authentication = getAuthentication(tokenRequestDto);
		RefreshToken refreshToken = getRefreshToken(tokenRequestDto.getRefreshToken(), authentication);

		refreshTokenMapper.deleteById(refreshToken.getId());
	}

	@Override
	@Transactional
	public TokenDto refresh(TokenRequestDto tokenRequestDto) {

		Authentication authentication = getAuthentication(tokenRequestDto);
		RefreshToken refreshToken = getRefreshToken(tokenRequestDto.getRefreshToken(), authentication);

		TokenDto tokenDto = tokenProvider.generateToken(authentication);

		refreshToken.updateRefreshTokenValue(tokenDto.getRefreshToken());
		refreshTokenMapper.update(refreshToken);

		return tokenDto;
  	}

	private RefreshToken getRefreshToken(String requestRefreshToken, Authentication authentication) {

		RefreshToken refreshToken = refreshTokenMapper.findByRefreshTokenKey(authentication.getName())
			.orElseThrow(() -> new UnauthorizedException("로그아웃한 사용자입니다."));

		if (!StringUtils.equals(refreshToken.getRefreshTokenValue(), requestRefreshToken)) {
			throw new UnauthorizedException("토큰의 유저 정보가 일치하지 않습니다.");
		}

		return refreshToken;
	}

	private Authentication getAuthentication(TokenRequestDto tokenRequestDto) {

		if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
			throw new ValidationException("Refresh Token이 유효하지 않습니다.");
		}

		return tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
	}
}
