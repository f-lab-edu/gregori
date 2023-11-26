package com.gregori.common.exception;

public class IllegalArgumentException extends BaseException {
	public IllegalArgumentException() {
		super(AppErrorMessage.ILLEGAL_ARGUMENT_ERROR);
	}

	public IllegalArgumentException(String description) {
		super(description, AppErrorMessage.ILLEGAL_ARGUMENT_ERROR);
	}
}
