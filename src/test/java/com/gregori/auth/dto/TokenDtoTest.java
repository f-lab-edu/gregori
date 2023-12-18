package com.gregori.auth.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenDtoTest {

	@Test
	@DisplayName("TokenDto 객체를 생성하면 get 메서드로 필드를 조회한다.")
	void should_getFields_when_createTokenDto() {

		// given
		TokenDto dto = TokenDto.builder()
			.grantType("type")
			.accessToken("accessToken")
			.refreshToken("refreshToken")
			.accessTokenExpiresIn(1L)
			.build();

		// when, then
		assertEquals(dto.getGrantType(), "type");
		assertEquals(dto.getAccessToken(), "accessToken");
		assertEquals(dto.getRefreshToken(), "refreshToken");
		assertEquals(dto.getAccessTokenExpiresIn(), 1L);
	}
}
