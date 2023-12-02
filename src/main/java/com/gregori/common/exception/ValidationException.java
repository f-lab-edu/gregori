package com.gregori.common.exception;

import static com.gregori.common.response.ErrorMessage.VALIDATION_ERROR;

public class ValidationException extends BaseException {
	public ValidationException() {
		super(VALIDATION_ERROR);
	}

	public ValidationException(String description) {
		super(description, VALIDATION_ERROR);
	}

}
