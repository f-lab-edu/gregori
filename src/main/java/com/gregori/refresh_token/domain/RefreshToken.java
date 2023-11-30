package com.gregori.refresh_token.domain;

import com.gregori.common.AbstractEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshToken extends AbstractEntity {
	private Long id;
	private String rtKey;
	private String rtValue;

	@Builder
	public RefreshToken(String rtKey, String rtValue) {
		this.rtKey = rtKey;
		this.rtValue = rtValue;
	}

	public void updateValue(String rtValue) {
		this.rtValue = rtValue;
	}
}
