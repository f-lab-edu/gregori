package com.gregori.seller.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.NoArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@NoArgsConstructor
class SellerTest {
	@Test
	@DisplayName("Seller 객체의 필드를 수정한다.")
	void updateSellerInfo() {
		// given
		Seller seller = Seller.builder()
			.memberId(1L)
			.businessNo("111-11-11111")
			.businessName("business name")
			.build();

		// when
		seller.updateSellerInfo("222-22-22222", "business name update");

		// then
		assertEquals(seller.getBusinessNo(), "222-22-22222");
		assertEquals(seller.getBusinessName(), "business name update");
	}

	@Test
	@DisplayName("Seller 객체의 필드를 builder 패턴으로 생성하고 getter 메서드로 조회한다.")
	void getterTest() {
		// given
		Seller seller = Seller.builder()
			.memberId(1L)
			.businessNo("123-45-67890")
			.businessName("business name")
			.build();

		// then
		assertNull(seller.getId());
		assertEquals(seller.getMemberId(), 1L);
		assertEquals(seller.getBusinessNo(), "123-45-67890");
		assertEquals(seller.getBusinessName(), "business name");
		assertNull(seller.getCreatedAt());
		assertNull(seller.getUpdatedAt());
	}
}
