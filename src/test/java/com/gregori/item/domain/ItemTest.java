package com.gregori.item.domain;

import static com.gregori.item.domain.Item.Status.END_OF_SALE;
import static com.gregori.item.domain.Item.Status.ON_SALE;
import static com.gregori.item.domain.Item.Status.PRE_SALE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ItemTest {

	@Test
	@DisplayName("Item 객체의 필드를 수정한다.")
	void updateItemInfo() {
		// given
		Item item = Item.builder()
			.sellerId(1L)
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
	@DisplayName("Item 객체의 상태를 'PRE_SALE'로 변경한다.")
	void preSale() {
		// given
		Item item = Item.builder()
			.sellerId(1L)
			.name("name")
			.price(1L)
			.inventory(10L)
			.build();
		item.onSale();
		Item.Status status = item.getStatus();

		// when
		item.preSale();

		// then
		assertEquals(status, ON_SALE);
		assertEquals(item.getStatus(), PRE_SALE);
	}

	@Test
	@DisplayName("Item 객체의 상태를 'ON_SALE'로 변경한다.")
	void onSale() {
		// given
		Item item = Item.builder()
			.sellerId(1L)
			.name("name")
			.price(1L)
			.inventory(10L)
			.build();
		Item.Status status = item.getStatus();

		// when
		item.onSale();

		// then
		assertEquals(status, PRE_SALE);
		assertEquals(item.getStatus(), ON_SALE);
	}

	@Test
	@DisplayName("Item 객체의 상태를 'END_OF_SALE'로 변경한다.")
	void endOfSale() {
		// given
		Item item = Item.builder()
			.sellerId(1L)
			.name("name")
			.price(1L)
			.inventory(10L)
			.build();
		Item.Status status = item.getStatus();
		// when
		item.endOfSale();

		// then
		assertEquals(status, PRE_SALE);
		assertEquals(item.getStatus(), END_OF_SALE);
	}

	@Test
	@DisplayName("Item 객체의 필드를 getter 메서드로 조회한다.")
	void getterTest() {
		// given
		Item item = new Item(1L, "name", 1L, 10L);

		// then
		assertNull(item.getId());
		assertEquals(item.getSellerId(), 1L);
		assertEquals(item.getName(), "name");
		assertEquals(item.getPrice(), 1L);
		assertEquals(item.getInventory(), 10L);
		assertEquals(item.getStatus().toString(), "PRE_SALE");
		assertNull(item.getCreatedAt());
		assertNull(item.getUpdatedAt());
	}

	@Test
	@DisplayName("Item 객체를 builder 패턴으로 생성한다.")
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
