package com.gregori.refresh_token.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenTest {

	@Test
	@DisplayName("RefreshToken 객체의 필드를 수정한다.")
	void should_updateRefreshTokenValue() {

		// given
		RefreshToken refreshToken = new RefreshToken("key", "value");

		// when
		refreshToken.updateRefreshTokenValue("new value");

		// then
		assertThat(refreshToken.getRefreshTokenValue()).isEqualTo("new value");
	}
}
