package com.gregori.common.exception;

public class UnauthorizedException extends BaseException {
	public UnauthorizedException() {
		super(AppErrorMessage.UNAUTHORIZED_ERROR);
	}

	public UnauthorizedException(String description) {
		super(description, AppErrorMessage.UNAUTHORIZED_ERROR);
	}

}
