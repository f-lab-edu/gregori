package com.gregori.common.exception;

import static com.gregori.common.response.ErrorMessage.ENTITY_DUPLICATE_ERROR;

public class DuplicateException extends BaseException {

	public DuplicateException() {
		super(ENTITY_DUPLICATE_ERROR);
	}

	public DuplicateException(String description) {
		super(description, ENTITY_DUPLICATE_ERROR);
	}
}
