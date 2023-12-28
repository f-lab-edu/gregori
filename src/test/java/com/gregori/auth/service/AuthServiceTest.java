package com.gregori.auth.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gregori.auth.dto.AuthSignInDto;
import com.gregori.member.domain.SessionMember;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@InjectMocks
	private AuthService authService;

	@Test
	@DisplayName("로그인을 성공하면 토큰을 발급한다.")
	void should_issueToken_when_signInSuccess() {

		// given
		AuthSignInDto authSignInDto = new AuthSignInDto("a@a.a", "aa11111!");

		// when
		SessionMember result = authService.signIn(authSignInDto);

		// then
		assertThat(result).isNotNull();
	}
}
