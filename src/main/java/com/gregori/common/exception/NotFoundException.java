package com.gregori.common.exception;

import static com.gregori.common.response.ErrorMessage.ENTITY_NOT_FOUND_ERROR;

public class NotFoundException extends BaseException {
	public NotFoundException() {
		super(ENTITY_NOT_FOUND_ERROR);
	}

	public NotFoundException(String description) {
		super(description, ENTITY_NOT_FOUND_ERROR);
	}
}
