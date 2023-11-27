package com.gregori.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
	private AppErrorMessage appErrorMessage;

	public BaseException() {
	}

	public BaseException(AppErrorMessage appErrorMessage) {
		super(appErrorMessage.getErrorMessage());
		this.appErrorMessage = appErrorMessage;
	}

	public BaseException(String description, AppErrorMessage appErrorMessage) {
		super(description);
		this.appErrorMessage = appErrorMessage;
	}

	public BaseException(String description, Throwable cause, AppErrorMessage appErrorMessage) {
		super(description, cause);
		this.appErrorMessage = appErrorMessage;
	}
}
