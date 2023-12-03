package com.gregori.item.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ItemTest {

	@Test
	@DisplayName("아이템 수정 메서드 테스트")
	void updateItemInfo() {
		// given
		Item item = Item.builder()
			.name("name")
			.price(1L)
			.inventory(10L)
			.build();

		// when
		item.updateItemInfo("name update", 9L, 99L);

		// then
		assertEquals(item.getName(), "name update");
		assertEquals(item.getPrice(), 9L);
		assertEquals(item.getInventory(), 99L);
	}

	@Test
	@DisplayName("아이템 상태 판매준비 메서드")
	void preSale() {
		// given
		Item item = Item.builder()
			.name("name")
			.price(1L)
			.inventory(10L)
			.build();

		item.onSale();

		// when
		item.preSale();

		// then
		assertEquals(item.getStatus().toString(), "PRE_SALE");
	}

	@Test
	@DisplayName("아이템 상태 판매중 메서드")
	void onSale() {
		// given
		Item item = Item.builder()
			.name("name")
			.price(1L)
			.inventory(10L)
			.build();

		// when
		item.onSale();

		// then
		assertEquals(item.getStatus().toString(), "ON_SALE");
	}

	@Test
	@DisplayName("아이템 상태 판매종료 메서드")
	void endOfSale() {
		// given
		Item item = Item.builder()
			.name("name")
			.price(1L)
			.inventory(10L)
			.build();

		// when
		item.preSale();

		// then
		assertEquals(item.getStatus().toString(), "PRE_SALE");
	}

	@Test
	@DisplayName("Item 도메인의 getter 테스트")
	void getterTest() {
		// given
		Item item = new Item("name", 1L, 10L);

		// then
		assertNull(item.getId());
		assertEquals(item.getName(), "name");
		assertEquals(item.getPrice(), 1L);
		assertEquals(item.getInventory(), 10L);
		assertEquals(item.getStatus().toString(), "PRE_SALE");
		assertNull(item.getCreatedAt());
		assertNull(item.getUpdatedAt());
	}

	@Test
	@DisplayName("Item 도메인의 builder 테스트")
	void builder() {
		// given
		Item item = Item.builder()
			.name("name")
			.price(1L)
			.inventory(10L)
			.build();

		// then
		assertNull(item.getId());
		assertEquals(item.getName(), "name");
		assertEquals(item.getPrice(), 1L);
		assertEquals(item.getInventory(), 10L);
		assertEquals(item.getStatus().toString(), "PRE_SALE");
		assertNull(item.getCreatedAt());
		assertNull(item.getUpdatedAt());
	}
}
