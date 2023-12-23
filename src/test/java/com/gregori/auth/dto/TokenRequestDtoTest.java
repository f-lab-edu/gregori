package com.gregori.auth.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenRequestDtoTest {

	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	@Test
	@DisplayName("올바른 입력값이면 TokenRequestDto 객체 생성에 성공한다.")
	void should_createTokenRequestDto_when_validInput() {

		// given
		TokenRequestDto dto = new TokenRequestDto("accessToken", "refreshToken");

		// when
		var result = validator.validate(dto);

		// then
		assertTrue(result.isEmpty());
	}

	@Test
	@DisplayName("accessToken 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void should_ValidException_when_blankAccessToken() {

		// given
		TokenRequestDto dto1 = new TokenRequestDto(null, "refreshToken");
		TokenRequestDto dto2 = new TokenRequestDto( "", "refreshToken");
		TokenRequestDto dto3 = new TokenRequestDto(" ", "refreshToken");

		// when
		var result1 = validator.validate(dto1);
		var result2 = validator.validate(dto2);
		var result3 = validator.validate(dto3);

		// then
		assertFalse(result1.isEmpty());
		assertFalse(result2.isEmpty());
		assertFalse(result3.isEmpty());
	}

	@Test
	@DisplayName("refreshToken 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void should_ValidException_when_blankRefreshToken() {

		// given
		TokenRequestDto dto1 = new TokenRequestDto("accessToken", null);
		TokenRequestDto dto2 = new TokenRequestDto("accessToken",  "");
		TokenRequestDto dto3 = new TokenRequestDto("accessToken", " ");

		// when
		var result1 = validator.validate(dto1);
		var result2 = validator.validate(dto2);
		var result3 = validator.validate(dto3);

		// then
		assertFalse(result1.isEmpty());
		assertFalse(result2.isEmpty());
		assertFalse(result3.isEmpty());
	}
}
