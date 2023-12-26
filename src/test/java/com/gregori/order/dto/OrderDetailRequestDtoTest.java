package com.gregori.order.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

class OrderDetailRequestDtoTest {

	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	@Test
	@DisplayName("입력값이 올바르면 OrderDetailRequestDto 객체 생성에 성공한다.")
	void should_createOrderDetailRequestDto_when_validInput() {

		// given
		OrderDetailRequestDto dto = new OrderDetailRequestDto(1L, 1L);

		//when
		var result = validator.validate(dto);

		//then
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("productId 필드가 비어 있으면 에러가 발생한다.")
	void should_ValidException_when_nullProductId() {

		// given
		OrderDetailRequestDto dto = new OrderDetailRequestDto(null, 1L);

		// when
		var result = validator.validate(dto);

		// then
		assertThat(result.isEmpty()).isFalse();
	}

	@Test
	@DisplayName("productCount 필드가 비어 있으면 에러가 발생한다.")
	void should_ValidException_when_nullProductCount() {

		// given
		OrderDetailRequestDto dto = new OrderDetailRequestDto(1L, null);

		// when
		var result = validator.validate(dto);

		// then
		assertThat(result.isEmpty()).isFalse();
	}
}
