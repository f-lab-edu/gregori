package com.gregori.common.exception;

import static com.gregori.common.response.ErrorMessage.UNAUTHORIZED_ERROR;

public class UnauthorizedException extends BaseException {

	public UnauthorizedException() {
		super(UNAUTHORIZED_ERROR);
	}

	public UnauthorizedException(String description) {
		super(description, UNAUTHORIZED_ERROR);
	}

}
