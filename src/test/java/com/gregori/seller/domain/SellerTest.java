package com.gregori.seller.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.gregori.seller.domain.Seller.Status.CLOSED;
import static com.gregori.seller.domain.Seller.Status.OPERATING;
import static org.assertj.core.api.Assertions.assertThat;

class SellerTest {
	@Test
	@DisplayName("Seller 객체의 필드를 수정한다.")
	void should_updateSellerInfo() {

		// given
		Seller seller = Seller.builder()
			.memberId(1L)
			.businessNumber("111-11-11111")
			.businessName("business name")
			.build();

		// when
		seller.updateSellerInfo("222-22-22222", "business name update");

		// then
		assertThat(seller.getBusinessNumber()).isEqualTo("222-22-22222");
		assertThat(seller.getBusinessName()).isEqualTo("business name update");
	}

	@Test
	@DisplayName("Seller 객체의 Status를 'OPERATING'로 변경한다.")
	void should_operating() {

		// given
		Seller seller = new Seller(1L, "123-45-67890", "businessName");
		seller.closed();
		Seller.Status status = seller.getStatus();

		// when
		seller.operating();

		// then
		assertThat(status).isEqualTo(CLOSED);
		assertThat(seller.getStatus()).isEqualTo(OPERATING);
	}

	@Test
	@DisplayName("Seller 객체의 Status를 'CLOSED'로 변경한다.")
	void should_closed() {

		// given
		Seller seller = new Seller(1L, "123-45-67890", "businessName");
		Seller.Status status = seller.getStatus();

		// when
		seller.closed();

		// then
		assertThat(status).isEqualTo(OPERATING);
		assertThat(seller.getStatus()).isEqualTo(CLOSED);
	}
}
