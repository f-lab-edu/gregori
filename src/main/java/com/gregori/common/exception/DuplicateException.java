package com.gregori.common.exception;

public class DuplicateException extends BaseException {
	public DuplicateException() {
		super(ErrorMessage.ENTITY_DUPLICATE_ERROR);
	}

	public DuplicateException(String description) {
		super(description, ErrorMessage.ENTITY_DUPLICATE_ERROR);
	}
}
