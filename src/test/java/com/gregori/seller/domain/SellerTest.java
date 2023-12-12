package com.gregori.seller.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.gregori.seller.domain.Seller.Status.CLOSED;
import static com.gregori.seller.domain.Seller.Status.OPERATING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SellerTest {
	@Test
	@DisplayName("Seller 객체의 필드를 수정한다.")
	void updateSellerInfo() {
		// given
		Seller seller = Seller.builder()
			.memberId(1L)
			.businessNumber("111-11-11111")
			.businessName("business name")
			.build();

		// when
		seller.updateSellerInfo("222-22-22222", "business name update");

		// then
		assertEquals(seller.getBusinessNumber(), "222-22-22222");
		assertEquals(seller.getBusinessName(), "business name update");
	}

	@Test
	@DisplayName("Seller 객체의 Status를 'OPERATING'로 변경한다.")
	void statusTest() {
		// given
		Seller seller = Seller.builder()
			.businessNumber("123-45-67890")
			.businessName("business name")
			.build();
		seller.closed();
		Seller.Status status = seller.getStatus();

		// when
		seller.operating();

		// then
		assertEquals(status, CLOSED);
		assertEquals(seller.getStatus(), OPERATING);
	}

	@Test
	@DisplayName("Seller 객체의 Status를 'CLOSED'로 변경한다.")
	void closed() {
		// given
		Seller seller = Seller.builder()
			.businessNumber("123-45-67890")
			.businessName("business name")
			.build();

		// when
		seller.closed();

		// then
		assertEquals(seller.getStatus(), CLOSED);
	}

	@Test
	@DisplayName("Seller 객체의 필드를 builder 패턴으로 생성하고 getter 메서드로 조회한다.")
	void getterTest() {
		// given
		Seller seller = Seller.builder()
			.memberId(1L)
			.businessNumber("123-45-67890")
			.businessName("business name")
			.build();

		// then
		assertNull(seller.getId());
		assertEquals(seller.getMemberId(), 1L);
		assertEquals(seller.getBusinessNumber(), "123-45-67890");
		assertEquals(seller.getBusinessName(), "business name");
		assertEquals(seller.getStatus(), OPERATING);
		assertNull(seller.getCreatedAt());
		assertNull(seller.getUpdatedAt());
	}
}
