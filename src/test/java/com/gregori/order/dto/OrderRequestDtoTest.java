package com.gregori.order.dto;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gregori.order.domain.Order;
import com.gregori.order_item.dto.OrderItemRequestDto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderRequestDtoTest {
	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();
	private final List<OrderItemRequestDto> orderItems = List.of(new OrderItemRequestDto(1L, 1L));

	@Test
	@DisplayName("OrderItem 객체 build 테스트")
	void toEntity() {
		// given
		OrderRequestDto dto = new OrderRequestDto(1L, "method", 1L, 1L, orderItems);

		// when
		Order order = dto.toEntity();

		// then
		assertEquals(order.getStatus().toString(), "ORDER_COMPLETED");
	}

	@Test
	@DisplayName("memberId 필드가 비어 있으면 에러 발생")
	void nullMemberIdInputFailsTest() {
		// given
		OrderRequestDto dto = new OrderRequestDto(null, "method", 1L, 1L, orderItems);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("paymentMethod 필드가 비어 있거나 빈 문자열이면 에러 발생")
	void blankPaymentMethodInputFailsTest() {
		// given
		OrderRequestDto dto1 = new OrderRequestDto(1L, null, 1L, 1L, orderItems);
		OrderRequestDto dto2 = new OrderRequestDto(1L, "", 1L, 1L, orderItems);
		OrderRequestDto dto3 = new OrderRequestDto(1L, " ", 1L, 1L, orderItems);

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
	@DisplayName("PaymentAmount 필드가 비어 있으면 에러 발생")
	void nullPaymentAmountInputFailsTest() {
		// given
		OrderRequestDto dto = new OrderRequestDto(1L, "method", null, 1L, orderItems);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("deliveryCost 필드가 비어 있으면 에러 발생")
	void nullDeliveryCostInputFailsTest() {
		// given
		OrderRequestDto dto = new OrderRequestDto(1L, "method", 1L, null, orderItems);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("orderItems 필드가 비어 있으면 에러 발생")
	void nullOrderItemsInputFailsTest() {
		// given
		OrderRequestDto dto = new OrderRequestDto(1L, "method", 1L, 1L, null);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("유효한 input이면 성공")
	void validInputSucceedsTest() {
		// given
		OrderRequestDto dto = new OrderRequestDto(1L, "method", 1L, 1L, orderItems);

		//when
		var result = validator.validate(dto);

		//then
		assertTrue(result.isEmpty());
	}
}
