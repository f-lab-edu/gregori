package com.gregori.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
	private ErrorMessage errorMessage;

	public BaseException() {
	}

	public BaseException(ErrorMessage errorMessage) {
		super(errorMessage.getErrorMessage());
		this.errorMessage = errorMessage;
	}

	public BaseException(String description, ErrorMessage errorMessage) {
		super(description);
		this.errorMessage = errorMessage;
	}

	public BaseException(String description, Throwable cause, ErrorMessage errorMessage) {
		super(description, cause);
		this.errorMessage = errorMessage;
	}
}
