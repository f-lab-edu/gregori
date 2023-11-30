package com.gregori.common.exception;

public class AccessDeniedException extends BaseException {
	public AccessDeniedException() {
		super(ErrorMessage.ACCESS_DENIED_ERROR);
	}

	public AccessDeniedException(String description) {
		super(description, ErrorMessage.ACCESS_DENIED_ERROR);
	}
}
