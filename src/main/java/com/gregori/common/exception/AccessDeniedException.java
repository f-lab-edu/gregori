package com.gregori.common.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@ResponseStatus(code = FORBIDDEN)
public class AccessDeniedException extends RuntimeException {

	public AccessDeniedException() {
		super("접근 권한이 없습니다.");
	}

	public AccessDeniedException(String message) {
		super(message);
	}
}
