package com.gregori.common.exception;

public class AccessDeniedException extends BaseException {
	public AccessDeniedException() {
		super(AppErrorMessage.ACCESS_DENIED_ERROR);
	}

	public AccessDeniedException(String description) {
		super(description, AppErrorMessage.ACCESS_DENIED_ERROR);
	}
}
