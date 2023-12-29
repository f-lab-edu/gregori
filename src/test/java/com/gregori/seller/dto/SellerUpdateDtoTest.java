package com.gregori.seller.dto;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

class SellerUpdateDtoTest {

	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	@Test
	@DisplayName("입력값이 올바르면 SellerUpdateDto 객체 생성에 성공한다.")
	void should_SellerUpdateDto_when_validInput() {

		// given
		SellerUpdateDto dto = new SellerUpdateDto(1L, "000-00-00000", "name");

		// when
		var result = validator.validate(dto);

		// then
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("id 필드가 비어 있으면 에러가 발생한다.")
	void should_ValidException_when_nullId() {

		// given
		SellerUpdateDto dto = new SellerUpdateDto(null, "000-00-00000", "name");

		// when
		var result = validator.validate(dto);

		// then
		assertThat(result.isEmpty()).isFalse();
	}

	@Test
	@DisplayName("businessNumber 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void should_ValidException_when_blankBusinessNumber() {

		// given
		SellerUpdateDto dto1 = new SellerUpdateDto(1L, null, "name");
		SellerUpdateDto dto2 = new SellerUpdateDto(1L, "", "name");
		SellerUpdateDto dto3 = new SellerUpdateDto(1L, " ", "name");

		// when
		var result1 = validator.validate(dto1);
		var result2 = validator.validate(dto2);
		var result3 = validator.validate(dto3);

		// then
		assertThat(result1.isEmpty()).isFalse();
		assertThat(result2.isEmpty()).isFalse();
		assertThat(result3.isEmpty()).isFalse();
	}

	@Test
	@DisplayName("businessNumber 필드의 패턴이 불일치하면 에러가 발생한다.")
	void should_ValidException_when_mismatchedBusinessNumber() {

		// given
		SellerUpdateDto dto = new SellerUpdateDto(1L, "00-00-00", "name");

		// when
		var result = validator.validate(dto);

		// then
		assertThat(result.isEmpty()).isFalse();
	}

	@Test
	@DisplayName("businessName 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void should_ValidException_when_blankBusinessName() {

		// given
		SellerUpdateDto dto1 = new SellerUpdateDto(1L, "000-00-00000", null);
		SellerUpdateDto dto2 = new SellerUpdateDto(1L, "000-00-00000", "");
		SellerUpdateDto dto3 = new SellerUpdateDto(1L, "000-00-00000", " ");

		// when
		var result1 = validator.validate(dto1);
		var result2 = validator.validate(dto2);
		var result3 = validator.validate(dto3);

		// then
		assertThat(result1.isEmpty()).isFalse();
		assertThat(result2.isEmpty()).isFalse();
		assertThat(result3.isEmpty()).isFalse();
	}
}
