package com.gregori.seller.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SellerRegisterDtoTest {

	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	@Test
	@DisplayName("memberId 필드가 비어 있으면 에러가 발생한다.")
	void should_ValidException_when_nullMemberId() {

		// given
		SellerRegisterDto dto = new SellerRegisterDto(null, "000-00-00000", "name");

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("businessNumber 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void should_ValidException_when_blankBusinessNumber() {

		// given
		SellerRegisterDto dto1 = new SellerRegisterDto(1L, null, "name");
		SellerRegisterDto dto2 = new SellerRegisterDto(1L, "", "name");
		SellerRegisterDto dto3 = new SellerRegisterDto(1L, " ", "name");

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
	@DisplayName("businessNumber 필드의 패턴이 불일치하면 에러가 발생한다.")
	void should_ValidException_when_mismatchedBusinessNumber() {

		// given
		SellerRegisterDto dto = new SellerRegisterDto(1L, "00-00-00", "name");

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("businessName 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void should_ValidException_when_blankBusinessName() {

		// given
		SellerRegisterDto dto1 = new SellerRegisterDto(1L, "000-00-00000", null);
		SellerRegisterDto dto2 = new SellerRegisterDto(1L, "000-00-00000", "");
		SellerRegisterDto dto3 = new SellerRegisterDto(1L, "000-00-00000", " ");

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
	@DisplayName("SellerRegisterDto 객체를 생성하면 private 필드를 get 메서드로 조회한다.")
	void should_getFields_when_createSellerRegisterDto() {

		// given
		SellerRegisterDto dto = new SellerRegisterDto(1L, "000-00-00000", "name");

		// when
		var result = validator.validate(dto);

		// then
		assertTrue(result.isEmpty());
	}
}
