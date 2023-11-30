package com.gregori.common.exception;

public class NotFoundException extends BaseException {
	public NotFoundException() {
		super(ErrorMessage.ENTITY_NOT_FOUND_ERROR);
	}

	public NotFoundException(String description) {
		super(description, ErrorMessage.ENTITY_NOT_FOUND_ERROR);
	}
}
