package com.gregori.item.dto;

import static com.gregori.item.domain.Item.Status.PRE_SALE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gregori.item.domain.Item;

class ItemResponseDtoTest {
	@Test
	@DisplayName("Item을 파라미터로 받아 builder 패턴으로 ItemResponseDto 객체를 생성한다.")
	void toEntityTest() {
		// given
		Item item = new Item(1L, "name", 1L, 1L);

		// when
		ItemResponseDto dto = new ItemResponseDto().toEntity(item);

		// then
		assertEquals(item.getName(), dto.getName());
		assertEquals(item.getPrice(), dto.getPrice());
		assertEquals(item.getInventory(), dto.getInventory());
	}

	@Test
	@DisplayName("ItemResponseDto 객체의 필드를 getter 메서드로 조회한다.")
	void getterTest() {
		// given
		ItemResponseDto dto = new ItemResponseDto(1L, 1L, "name", 1L, 10L, PRE_SALE);

		// then
		assertEquals(dto.getId(), 1L);
		assertEquals(dto.getName(), "name");
		assertEquals(dto.getPrice(), 1L);
		assertEquals(dto.getInventory(), 10L);
		assertEquals(dto.getStatus().toString(), "PRE_SALE");
	}
}
