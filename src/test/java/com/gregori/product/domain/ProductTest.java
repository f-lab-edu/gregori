package com.gregori.product.domain;

import static com.gregori.product.domain.Product.Status.END_OF_SALE;
import static com.gregori.product.domain.Product.Status.ON_SALE;
import static com.gregori.product.domain.Product.Status.PRE_SALE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

	@Test
	@DisplayName("Product 객체의 필드를 수정한다.")
	void should_updateProductInfo() {

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
		assertThat(product.getName()).isEqualTo("name update");
		assertThat(product.getPrice()).isEqualTo(9L);
		assertThat(product.getInventory()).isEqualTo(99L);
	}

	@Test
	@DisplayName("Product 객체의 상태를 'PRE_SALE'로 변경한다.")
	void should_preSale() {

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
		assertThat(status).isEqualTo(ON_SALE);
		assertThat(product.getStatus()).isEqualTo(PRE_SALE);
	}

	@Test
	@DisplayName("Product 객체의 상태를 'ON_SALE'로 변경한다.")
	void should_onSale() {

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
		assertThat(status).isEqualTo(PRE_SALE);
		assertThat(product.getStatus()).isEqualTo(ON_SALE);
	}

	@Test
	@DisplayName("Product 객체의 상태를 'END_OF_SALE'로 변경한다.")
	void should_endOfSale() {

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
		assertThat(status).isEqualTo(PRE_SALE);
		assertThat(product.getStatus()).isEqualTo(END_OF_SALE);
	}
}
