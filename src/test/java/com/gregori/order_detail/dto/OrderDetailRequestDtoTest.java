package com.gregori.order_detail.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gregori.product.domain.Product;
import com.gregori.order_detail.domain.OrderDetail;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderDetailRequestDtoTest {

	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	@Test
	@DisplayName("OrderDetail 객체를 builder 패턴으로 생성한다.")
	void toEntity() {

		// given
		OrderDetailRequestDto dto = new OrderDetailRequestDto(1L, 1L);

		// when
		Product product = Product.builder()
			.name("name")
			.price(1L)
			.inventory(1L)
			.build();
		OrderDetail orderDetail = dto.toEntity(1L, product);

		// then
		assertEquals(orderDetail.getStatus().toString(), "PAYMENT_COMPLETED");
	}

	@Test
	@DisplayName("productId 필드가 비어 있으면 에러가 발생한다.")
	void nullProductIdInputFailsTest() {

		// given
		OrderDetailRequestDto dto = new OrderDetailRequestDto(null, 1L);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("productCount 필드가 비어 있으면 에러가 발생한다.")
	void nullProductCountCountInputFailsTest() {

		// given
		OrderDetailRequestDto dto = new OrderDetailRequestDto(1L, null);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("입력값이 올바르면 OrderDetailRequestDto 객체 생성에 성공한다.")
	void validInputSucceedsTest() {

		// given
		OrderDetailRequestDto dto = new OrderDetailRequestDto(1L, 1L);

		//when
		var result = validator.validate(dto);

		//then
		assertTrue(result.isEmpty());
	}
}
