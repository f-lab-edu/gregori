package com.gregori.common.exception;

public class DuplicateException extends BaseException {
	public DuplicateException() {
		super(AppErrorMessage.ENTITY_DUPLICATE_ERROR);
	}

	public DuplicateException(String description) {
		super(description, AppErrorMessage.ENTITY_DUPLICATE_ERROR);
	}
}
