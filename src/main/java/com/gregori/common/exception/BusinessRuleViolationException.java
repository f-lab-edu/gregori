package com.gregori.common.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

@ResponseStatus(code = CONFLICT)
public class BusinessRuleViolationException extends RuntimeException {

	public BusinessRuleViolationException() {
		super("비즈니스 룰을 위반했습니다.");
	}

	public BusinessRuleViolationException(String message) {
		super(message);
	}
}
