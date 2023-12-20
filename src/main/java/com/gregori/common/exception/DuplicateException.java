package com.gregori.common.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

@ResponseStatus(code = CONFLICT)
public class DuplicateException extends RuntimeException {

	public DuplicateException() {
		super("중복된 값입니다.");
	}

	public DuplicateException(String message) {
		super(message);
	}
}
