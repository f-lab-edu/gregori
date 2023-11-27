package com.gregori.common.exception;

public class NotFoundException extends BaseException {
	public NotFoundException() {
		super(AppErrorMessage.ENTITY_NOT_FOUND_ERROR);
	}

	public NotFoundException(String description) {
		super(description, AppErrorMessage.ENTITY_NOT_FOUND_ERROR);
	}
}
