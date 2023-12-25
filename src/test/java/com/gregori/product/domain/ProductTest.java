package com.gregori.product.domain;

import static com.gregori.product.domain.Product.Status.ON_SALE;
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
			.categoryId(1L)
			.name("name")
			.price(1L)
			.inventory(1L)
			.build();

		// when
		product.updateProductInfo(2L, "newName", 2L, 2L, ON_SALE);

		// then
		assertThat(product.getCategoryId()).isEqualTo(2L);
		assertThat(product.getName()).isEqualTo("newName");
		assertThat(product.getPrice()).isEqualTo(2L);
		assertThat(product.getInventory()).isEqualTo(2L);
		assertThat(product.getStatus()).isEqualTo(ON_SALE);
	}
}
