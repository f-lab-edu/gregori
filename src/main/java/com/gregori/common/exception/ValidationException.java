package com.gregori.common.exception;

public class ValidationException extends BaseException {
	public ValidationException() {
		super(AppErrorMessage.VALIDATION_ERROR);
	}

	public ValidationException(String description) {
		super(description, AppErrorMessage.VALIDATION_ERROR);
	}

}
