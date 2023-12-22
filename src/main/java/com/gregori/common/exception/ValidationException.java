package com.gregori.common.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(code = BAD_REQUEST)
public class ValidationException extends RuntimeException {

	public ValidationException() {
		super("유효하지 않은 값입니다.");
	}

	public ValidationException(String message) {
		super(message);
	}
}
