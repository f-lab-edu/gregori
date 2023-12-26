package com.gregori.common.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IsDeleted {

	TRUE("삭제됨"),
	FALSE("삭제되지 않음");

	private final String description;
}
