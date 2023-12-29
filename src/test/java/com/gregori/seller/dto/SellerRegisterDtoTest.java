package com.gregori.seller.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

class SellerRegisterDtoTest {

	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	@Test
	@DisplayName("입력값이 올바르면 SellerRegisterDto 객체 생성에 성공한다.")
	void should_craeteSellerRegisterDto_when_validInput() {

		// given
		SellerRegisterDto dto = new SellerRegisterDto("000-00-00000", "name");

		// when
		var result = validator.validate(dto);

		// then
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("businessNumber 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void should_ValidException_when_blankBusinessNumber() {

		// given
		SellerRegisterDto dto1 = new SellerRegisterDto(null, "name");
		SellerRegisterDto dto2 = new SellerRegisterDto("", "name");
		SellerRegisterDto dto3 = new SellerRegisterDto(" ", "name");

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
		SellerRegisterDto dto = new SellerRegisterDto("00-00-00", "name");

		// when
		var result = validator.validate(dto);

		// then
		assertThat(result.isEmpty()).isFalse();
	}

	@Test
	@DisplayName("businessName 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void should_ValidException_when_blankBusinessName() {

		// given
		SellerRegisterDto dto1 = new SellerRegisterDto("000-00-00000", null);
		SellerRegisterDto dto2 = new SellerRegisterDto("000-00-00000", "");
		SellerRegisterDto dto3 = new SellerRegisterDto("000-00-00000", " ");

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
