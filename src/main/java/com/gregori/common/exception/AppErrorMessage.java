package com.gregori.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppErrorMessage {
	ENTITY_NOT_FOUND_ERROR("존재하지 않는 엔티티입니다."),
	ENTITY_DUPLICATE_ERROR("이미 존재하는 엔티티입니다.");

	private final String description;

	public String getErrorMessage(Object... arg) {
		return String.format(description, arg);
	}
}
