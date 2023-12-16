package com.gregori.common.exception;

import static com.gregori.common.response.ErrorMessage.BUSINESS_RULE_VIOLATION_ERROR;

public class BusinessRuleViolationException extends BaseException {
	public BusinessRuleViolationException() {
		super(BUSINESS_RULE_VIOLATION_ERROR);
	}

	public BusinessRuleViolationException(String description) {
		super(description, BUSINESS_RULE_VIOLATION_ERROR);
	}
}
