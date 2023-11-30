package com.gregori.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
	SYSTEM_ERROR("시스템 에러가 발생했습니다."),
	UNAUTHORIZED_ERROR("인증되지 않은 사용자입니다."),
	ACCESS_DENIED_ERROR("접근이 거부되었습니다."),
	INVALID_PARAMETER_ERROR("요청한 값이 올바르지 않습니다."),
	VALIDATION_ERROR("유효한 값이 아닙니다."),
	ENTITY_NOT_FOUND_ERROR("존재하지 않는 엔티티입니다."),
	ENTITY_DUPLICATE_ERROR("중복되는 엔티티입니다.");

	private final String description;

	public String getErrorMessage(Object... arg) {
		return String.format(description, arg);
	}
}
