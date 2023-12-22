package com.gregori.common.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ResponseStatus(code = UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

	public UnauthorizedException() {
		super("권한이 없습니다.");
	}

	public UnauthorizedException(String message) {
		super(message);
	}
}
