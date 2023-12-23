package com.gregori.order.dto;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gregori.order.domain.Order;
import com.gregori.order_detail.dto.OrderDetailRequestDto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static com.gregori.order.domain.Order.Status.ORDER_PROCESSING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderRequestDtoTest {

	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();
	private final List<OrderDetailRequestDto> orderDetails = List.of(new OrderDetailRequestDto(1L, 1L));

	@Test
	@DisplayName("memberId 필드가 비어 있으면 에러가 발생한다.")
	void should_ValidException_when_nullMemberId() {

		// given
		OrderRequestDto dto = new OrderRequestDto(null, "method", 1L, 1L, orderDetails);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
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
		assertFalse(result1.isEmpty());
		assertFalse(result2.isEmpty());
		assertFalse(result3.isEmpty());
	}

	@Test
	@DisplayName("PaymentAmount 필드가 비어 있으면 에러가 발생한다.")
	void should_ValidException_when_nullPaymentAmount() {

		// given
		OrderRequestDto dto = new OrderRequestDto(1L, "method", null, 1L, orderDetails);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("deliveryCost 필드가 비어 있으면 에러가 발생한다.")
	void should_ValidException_when_nullDeliveryCost() {

		// given
		OrderRequestDto dto = new OrderRequestDto(1L, "method", 1L, null, orderDetails);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("orderDetails 필드가 비어 있으면 에러가 발생한다.")
	void should_ValidException_when_nullOrderDetails() {

		// given
		OrderRequestDto dto = new OrderRequestDto(1L, "method", 1L, 1L, null);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("입력값이 올바르면 OrderRequestDto 객체 생성에 성공한다.")
	void should_craeteOrderRequestDto_when_validInput() {

		// given
		OrderRequestDto dto = new OrderRequestDto(1L, "method", 1L, 1L, orderDetails);

		//when
		var result = validator.validate(dto);

		//then
		assertTrue(result.isEmpty());
	}

	@Test
	@DisplayName("AuthSignInDto 객체를 생성하면 private 필드를 get 메서드로 조회한다.")
	void should_getFields_when_createAuthSignInDto() {

		// given
		OrderRequestDto dto = new OrderRequestDto(1L, "method", 1L, 1L, orderDetails);

		// when
		Order order = dto.toEntity();

		// then
		assertEquals(order.getStatus(), ORDER_PROCESSING);
	}
}
