package com.gregori.auth.service;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.gregori.auth.dto.AuthSignInDto;
import com.gregori.auth.dto.TokenDto;
import com.gregori.auth.dto.TokenRequestDto;
import com.gregori.common.exception.NotFoundException;
import com.gregori.config.jwt.TokenProvider;
import com.gregori.refresh_token.domain.RefreshToken;
import com.gregori.refresh_token.mapper.RefreshTokenMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

	@Mock
	private RefreshTokenMapper refreshTokenMapper;
	@Mock
	private TokenProvider tokenProvider;
	@Mock
	private AuthenticationManager authManager;

	@InjectMocks
	private AuthServiceImpl authService;

	@Test
	@DisplayName("로그인을 성공하면 토큰을 발급한다.")
	void should_issueToken_when_signInSuccess() {

		// given
		AuthSignInDto authSignInDto = new AuthSignInDto("a@a.a", "aa11111!");
		TokenDto tokenDto = new TokenDto("type", "accessToken", "refreshToken", 1L);

		UsernamePasswordAuthenticationToken authToken = authSignInDto.toAuthentication();
		Authentication authentication = mock(Authentication.class);
		given(authManager.authenticate(authToken)).willReturn(authentication);
		given(tokenProvider.generateToken(authentication)).willReturn(tokenDto);
		given(refreshTokenMapper.findByRefreshTokenKey(null)).willReturn(Optional.empty());

		// when
		TokenDto result = authService.signIn(authSignInDto);

		// then
		assertEquals(result, tokenDto);

		verify(authManager).authenticate(authToken);
		verify(tokenProvider).generateToken(authentication);

		verify(refreshTokenMapper).findByRefreshTokenKey(null);
		verify(refreshTokenMapper).insert(any(RefreshToken.class));
	}

	@Test
	@DisplayName("로그아웃을 성공하면 리프레시 토큰을 삭제한다.")
	void should_revokeAuthorization_when_signOutSuccess() {

		// given
		TokenRequestDto tokenRequestDto = new TokenRequestDto("accessToken", "refreshToken");
		Authentication authentication = mock(Authentication.class);
		RefreshToken refreshToken = new RefreshToken("accessToken", "refreshToken");

		given(tokenProvider.validateToken(tokenRequestDto.getRefreshToken())).willReturn(true);
		given(tokenProvider.getAuthentication(tokenRequestDto.getAccessToken())).willReturn(authentication);
		given(refreshTokenMapper.findByRefreshTokenKey(null)).willReturn(Optional.of(refreshToken));

		// when
		authService.signOut(tokenRequestDto);

		// then
		verify(tokenProvider).validateToken(tokenRequestDto.getRefreshToken());
		verify(tokenProvider).getAuthentication(tokenRequestDto.getAccessToken());

		verify(refreshTokenMapper).findByRefreshTokenKey(null);
		verify(refreshTokenMapper).deleteById(null);
	}

	@Test
	@DisplayName("리프레시 토큰 재발급을 성공하면 토큰을 업데이트한다.")
	void should_updateToken_when_refreshSuccess() {

		// given
		TokenRequestDto tokenRequestDto = new TokenRequestDto("accessToken", "refreshToken");
		RefreshToken refreshToken = new RefreshToken("accessToken", "refreshToken");
		TokenDto tokenDto = new TokenDto("type", "accessToken2", "refreshToken2", 1L);
		Authentication authentication = mock(Authentication.class);

		given(tokenProvider.validateToken(tokenRequestDto.getRefreshToken())).willReturn(true);
		given(tokenProvider.getAuthentication(tokenRequestDto.getAccessToken())).willReturn(authentication);
		given(tokenProvider.generateToken(authentication)).willReturn(tokenDto);
		given(refreshTokenMapper.findByRefreshTokenKey(null)).willReturn(Optional.of(refreshToken));

		// when
		authService.refresh(tokenRequestDto);

		// then
		verify(tokenProvider).validateToken(tokenRequestDto.getRefreshToken());
		verify(tokenProvider).getAuthentication(tokenRequestDto.getAccessToken());
		verify(tokenProvider).generateToken(authentication);

		verify(refreshTokenMapper).findByRefreshTokenKey(null);
		verify(refreshTokenMapper).update(refreshToken);
	}
}
