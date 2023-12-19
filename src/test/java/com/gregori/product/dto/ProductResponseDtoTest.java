package com.gregori.product.dto;

import static com.gregori.product.domain.Product.Status.PRE_SALE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gregori.product.domain.Product;

class ProductResponseDtoTest {
	@Test
	@DisplayName("Item을 파라미터로 받아 builder 패턴으로 ItemResponseDto 객체를 생성한다.")
	void toEntityTest() {
		// given
		Product product = new Product(1L, "name", 1L, 1L);

		// when
		ProductResponseDto dto = new ProductResponseDto().toEntity(product);

		// then
		assertEquals(product.getName(), dto.getName());
		assertEquals(product.getPrice(), dto.getPrice());
		assertEquals(product.getInventory(), dto.getInventory());
	}

	@Test
	@DisplayName("ItemResponseDto 객체의 필드를 getter 메서드로 조회한다.")
	void getterTest() {
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
