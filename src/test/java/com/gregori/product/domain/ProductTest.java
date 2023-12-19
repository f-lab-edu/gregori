package com.gregori.product.domain;

import static com.gregori.product.domain.Product.Status.END_OF_SALE;
import static com.gregori.product.domain.Product.Status.ON_SALE;
import static com.gregori.product.domain.Product.Status.PRE_SALE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

	@Test
	@DisplayName("Item 객체의 필드를 수정한다.")
	void updateItemInfo() {

		// given
		Product product = Product.builder()
			.sellerId(1L)
			.name("name")
			.price(1L)
			.inventory(10L)
			.build();

		// when
		product.updateProductInfo("name update", 9L, 99L);

		// then
		assertEquals(product.getName(), "name update");
		assertEquals(product.getPrice(), 9L);
		assertEquals(product.getInventory(), 99L);
	}

	@Test
	@DisplayName("Item 객체의 상태를 'PRE_SALE'로 변경한다.")
	void preSale() {

		// given
		Product product = Product.builder()
			.sellerId(1L)
			.name("name")
			.price(1L)
			.inventory(10L)
			.build();
		product.onSale();
		Product.Status status = product.getStatus();

		// when
		product.preSale();

		// then
		assertEquals(status, ON_SALE);
		assertEquals(product.getStatus(), PRE_SALE);
	}

	@Test
	@DisplayName("Item 객체의 상태를 'ON_SALE'로 변경한다.")
	void onSale() {

		// given
		Product product = Product.builder()
			.sellerId(1L)
			.name("name")
			.price(1L)
			.inventory(10L)
			.build();
		Product.Status status = product.getStatus();

		// when
		product.onSale();

		// then
		assertEquals(status, PRE_SALE);
		assertEquals(product.getStatus(), ON_SALE);
	}

	@Test
	@DisplayName("Item 객체의 상태를 'END_OF_SALE'로 변경한다.")
	void endOfSale() {

		// given
		Product product = Product.builder()
			.sellerId(1L)
			.name("name")
			.price(1L)
			.inventory(10L)
			.build();
		Product.Status status = product.getStatus();
		// when
		product.endOfSale();

		// then
		assertEquals(status, PRE_SALE);
		assertEquals(product.getStatus(), END_OF_SALE);
	}

	@Test
	@DisplayName("Item 객체의 필드를 getter 메서드로 조회한다.")
	void getterTest() {

		// given
		Product product = new Product(1L, "name", 1L, 10L);

		// then
		assertNull(product.getId());
		assertEquals(product.getSellerId(), 1L);
		assertEquals(product.getName(), "name");
		assertEquals(product.getPrice(), 1L);
		assertEquals(product.getInventory(), 10L);
		assertEquals(product.getStatus().toString(), "PRE_SALE");
		assertNull(product.getCreatedAt());
		assertNull(product.getUpdatedAt());
	}

	@Test
	@DisplayName("Item 객체를 builder 패턴으로 생성한다.")
	void builder() {

		// given
		Product product = Product.builder()
			.name("name")
			.price(1L)
			.inventory(10L)
			.build();

		// then
		assertNull(product.getId());
		assertEquals(product.getName(), "name");
		assertEquals(product.getPrice(), 1L);
		assertEquals(product.getInventory(), 10L);
		assertEquals(product.getStatus().toString(), "PRE_SALE");
		assertNull(product.getCreatedAt());
		assertNull(product.getUpdatedAt());
	}
}
