package com.gregori.order.dto;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

class OrderRequestDtoTest {

	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();
	private final List<OrderDetailRequestDto> orderDetails = List.of(new OrderDetailRequestDto(1L, 1L));

	@Test
	@DisplayName("입력값이 올바르면 OrderRequestDto 객체 생성에 성공한다.")
	void should_craeteOrderRequestDto_when_validInput() {

		// given
		OrderRequestDto dto = new OrderRequestDto(1L, "method", 1L, 1L, orderDetails);

		//when
		var result = validator.validate(dto);

		//then
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("memberId 필드가 비어 있으면 에러가 발생한다.")
	void should_ValidException_when_nullMemberId() {

		// given
		OrderRequestDto dto = new OrderRequestDto(null, "method", 1L, 1L, orderDetails);

		// when
		var result = validator.validate(dto);

		// then
		assertThat(result.isEmpty()).isFalse();
	}

	@Test
	@DisplayName("paymentMethod 필드가 비어 있거나 빈 문자열이면 에러가 발생한다.")
	void should_ValidException_when_blankPaymentMethod() {

		// given
		OrderRequestDto dto1 = new OrderRequestDto(1L, null, 1L, 1L, orderDetails);
		OrderRequestDto dto2 = new OrderRequestDto(1L, "", 1L, 1L, orderDetails);
		OrderRequestDto dto3 = new OrderRequestDto(1L, " ", 1L, 1L, orderDetails);

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
	@DisplayName("PaymentAmount 필드가 비어 있으면 에러가 발생한다.")
	void should_ValidException_when_nullPaymentAmount() {

		// given
		OrderRequestDto dto = new OrderRequestDto(1L, "method", null, 1L, orderDetails);

		// when
		var result = validator.validate(dto);

		// then
		assertThat(result.isEmpty()).isFalse();
	}

	@Test
	@DisplayName("deliveryCost 필드가 비어 있으면 에러가 발생한다.")
	void should_ValidException_when_nullDeliveryCost() {

		// given
		OrderRequestDto dto = new OrderRequestDto(1L, "method", 1L, null, orderDetails);

		// when
		var result = validator.validate(dto);

		// then
		assertThat(result.isEmpty()).isFalse();
	}

	@Test
	@DisplayName("orderDetails 필드가 비어 있으면 에러가 발생한다.")
	void should_ValidException_when_nullOrderDetails() {

		// given
		OrderRequestDto dto = new OrderRequestDto(1L, "method", 1L, 1L, null);

		// when
		var result = validator.validate(dto);

		// then
		assertThat(result.isEmpty()).isFalse();
	}
}
