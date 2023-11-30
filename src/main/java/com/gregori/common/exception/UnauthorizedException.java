package com.gregori.common.exception;

public class UnauthorizedException extends BaseException {
	public UnauthorizedException() {
		super(ErrorMessage.UNAUTHORIZED_ERROR);
	}

	public UnauthorizedException(String description) {
		super(description, ErrorMessage.UNAUTHORIZED_ERROR);
	}

}
