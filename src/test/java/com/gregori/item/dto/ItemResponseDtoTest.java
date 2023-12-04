package com.gregori.item.dto;

import static com.gregori.item.domain.Item.Status.PRE_SALE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gregori.item.domain.Item;

class ItemResponseDtoTest {
	@Test
	@DisplayName("ItemResponseDto의 build 테스트")
	void toEntityTest() {
		// given
		Item item = new Item("name", 1L, 1L);

		// when
		ItemResponseDto dto = new ItemResponseDto().toEntity(item);

		// then
		assertEquals(item.getName(), dto.getName());
		assertEquals(item.getPrice(), dto.getPrice());
		assertEquals(item.getInventory(), dto.getInventory());
	}

	@Test
	@DisplayName("ItemResponseDto의 getter 테스트")
	void getterTest() {
		// given
		ItemResponseDto dto = new ItemResponseDto(1L, "name", 1L, 10L, PRE_SALE);

		// then
		assertEquals(dto.getId(), 1L);
		assertEquals(dto.getName(), "name");
		assertEquals(dto.getPrice(), 1L);
		assertEquals(dto.getInventory(), 10L);
		assertEquals(dto.getStatus().toString(), "PRE_SALE");
	}
}
