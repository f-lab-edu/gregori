package com.gregori.order_item.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.gregori.item.domain.Item;
import com.gregori.order_item.domain.OrderItem;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class OrderItemRequestDtoTest {
	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	@Test
	@DisplayName("OrderItem 객체 build 테스트")
	void toEntity() {
		// given
		OrderItemRequestDto dto = new OrderItemRequestDto(1L, 1L);

		// when
		Item item = Item.builder().build();
		OrderItem orderItem = dto.toEntity(1L, item);

		// then
		assertEquals(orderItem.getStatus().toString(), "PAYMENT_COMPLETED");
	}

	@Test
	@DisplayName("orderItem 필드가 비어 있으면 에러 발생")
	void nullOrderCountInputFailsTest() {
		// given
		OrderItemRequestDto dto = new OrderItemRequestDto(null, 1L);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("itemId 필드가 비어 있으면 에러 발생")
	void nullItemIdInputFailsTest() {
		// given
		OrderItemRequestDto dto = new OrderItemRequestDto(1L, null);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("유효한 input이면 성공")
	void validInputSucceedsTest() {
		// given
		OrderItemRequestDto dto = new OrderItemRequestDto(1L, 1L);

		//when
		var result = validator.validate(dto);

		//then
		assertTrue(result.isEmpty());
	}
}
