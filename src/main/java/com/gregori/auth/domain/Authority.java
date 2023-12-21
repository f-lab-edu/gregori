package com.gregori.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Authority {

	GENERAL_MEMBER("일반 회원"),
	SELLING_MEMBER("판매 회원"),
	ADMIN_MEMBER("관리자");

	private final String description;
}
