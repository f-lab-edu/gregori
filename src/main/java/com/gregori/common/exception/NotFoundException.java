package com.gregori.common.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(code = NOT_FOUND)
public class NotFoundException extends RuntimeException {

	public NotFoundException() {
		super("요청한 값을 찾을 수 없습니다.");
	}

	public NotFoundException(String message) {
		super(message);
	}
}
