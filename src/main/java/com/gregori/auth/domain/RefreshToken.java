package com.gregori.auth.domain;

import com.gregori.common.AbstractEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshToken extends AbstractEntity {

	private Long id;
	private String refreshTokenKey;
	private String refreshTokenValue;

	@Builder
	public RefreshToken(String refreshTokenKey, String refreshTokenValue) {

		this.refreshTokenKey = refreshTokenKey;
		this.refreshTokenValue = refreshTokenValue;
	}

	public void updateRefreshTokenValue(String refreshTokenValue) {

		this.refreshTokenValue = refreshTokenValue;
	}
}
