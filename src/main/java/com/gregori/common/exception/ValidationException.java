package com.gregori.common.exception;

public class ValidationException extends BaseException {
	public ValidationException() {
		super(ErrorMessage.VALIDATION_ERROR);
	}

	public ValidationException(String description) {
		super(description, ErrorMessage.VALIDATION_ERROR);
	}

}
