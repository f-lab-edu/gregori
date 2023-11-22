package com.gregori.domain.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Session {
	private String id;
	private Long memberId;

	@Builder
	public Session(String id, Long memberId) {
		this.id = id;
		this.memberId = memberId;
	}
}
