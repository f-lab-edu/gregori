package com.gregori.refresh_token.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RefreshTokenTest {

	@Test
	@DisplayName("RefreshToken 객체의 필드를 수정한다.")
	void should_updateRefreshTokenValue() {
		// given
		RefreshToken refreshToken = new RefreshToken("key", "value");

		// when
		refreshToken.updateRefreshTokenValue("new value");

		// then
		assertEquals(refreshToken.getRefreshTokenValue(), "new value");
	}

	@Test
	@DisplayName("Member 객체의 필드를 builder 패턴으로 생성하고 getter 메서드로 조회한다.")
	void should_getFields_when_buildMember() {
		// given
		RefreshToken refreshToken = RefreshToken.builder()
			.refreshTokenKey("key")
			.refreshTokenValue("value")
			.build();

		// when, then
		assertNull(refreshToken.getId());
		assertEquals(refreshToken.getRefreshTokenKey(), "key");
		assertEquals(refreshToken.getRefreshTokenValue(), "value");
		assertNull(refreshToken.getCreatedAt());
		assertNull(refreshToken.getUpdatedAt());
	}
}
