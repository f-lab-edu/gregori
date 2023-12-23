package com.gregori.product.dto;

import static com.gregori.product.domain.Product.Status.PRE_SALE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductResponseDtoTest {

	@Test
	@DisplayName("ItemResponseDto 객체를 생성하면 private 필드를 get 메서드로 조회한다.")
	void should_getFields_when_createProductResponseDto() {

		// given
		ProductResponseDto dto = new ProductResponseDto(1L, 1L, "name", 1L, 10L, PRE_SALE);

		// then
		assertEquals(dto.getId(), 1L);
		assertEquals(dto.getName(), "name");
		assertEquals(dto.getPrice(), 1L);
		assertEquals(dto.getInventory(), 10L);
		assertEquals(dto.getStatus().toString(), "PRE_SALE");
	}
}
