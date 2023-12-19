package com.gregori.common.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

	SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예기치 못한 시스템 에러가 발생했습니다."),
	UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
	ACCESS_DENIED_ERROR(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
	INVALID_PARAMETER_ERROR(HttpStatus.BAD_REQUEST, "요청한 값이 올바르지 않습니다."),
	VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "유효한 값이 아닙니다."),
	ENTITY_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "존재하지 않는 엔티티입니다."),
	ENTITY_DUPLICATE_ERROR(HttpStatus.CONFLICT, "중복되는 엔티티입니다."),
	BUSINESS_RULE_VIOLATION_ERROR(HttpStatus.CONFLICT, "비즈니스 룰을 위반했습니다.");

	private final HttpStatus httpStatus;
	private final String description;
}
