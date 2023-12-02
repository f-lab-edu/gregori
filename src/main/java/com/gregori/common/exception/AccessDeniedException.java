package com.gregori.common.exception;

import static com.gregori.common.response.ErrorMessage.ACCESS_DENIED_ERROR;

public class AccessDeniedException extends BaseException {
	public AccessDeniedException() {
		super(ACCESS_DENIED_ERROR);
	}

	public AccessDeniedException(String description) {
		super(description, ACCESS_DENIED_ERROR);
	}
}
