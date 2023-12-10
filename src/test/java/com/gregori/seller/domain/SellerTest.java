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
			.name("name")
			.email("a@a.a")
			.password("aa11111!")
			.businessNo("123-45-67890")
			.businessName("business name")
			.build();

		// when
		seller.updateSellerInfo("name update", "bb22222@", "business name update");

		// then
		assertEquals(seller.getName(), "name update");
		assertEquals(seller.getPassword(), "bb22222@");
		assertEquals(seller.getBusinessName(), "business name update");
	}

	@Test
	@DisplayName("Seller 객체의 상태를 'ACTIAVTE'로 변경한다.")
	void activate() {
		// given
		Seller seller = Seller.builder()
			.name("name")
			.email("a@a.a")
			.password("aa11111!")
			.businessNo("123-45-67890")
			.businessName("business name")
			.build();
		seller.deactivate();

		// when
		seller.activate();

		// then
		assertEquals(seller.getStatus().toString(), "ACTIVATE");
	}

	@Test
	@DisplayName("Seller 객체의 상태를 'DEACTIVATE'로 변경한다.")
	void deactivate() {
		// given
		Seller seller = Seller.builder()
			.name("name")
			.email("a@a.a")
			.password("aa11111!")
			.businessNo("123-45-67890")
			.businessName("business name")
			.build();

		// when
		seller.deactivate();

		// then
		assertEquals(seller.getStatus().toString(), "DEACTIVATE");
	}


	@Test
	@DisplayName("Seller 객체의 필드를 builder 패턴으로 생성하고 getter 메서드로 조회한다.")
	void getterTest() {
		// given
		Seller seller = Seller.builder()
			.name("name")
			.email("a@a.a")
			.password("aa11111!")
			.businessNo("123-45-67890")
			.businessName("business name")
			.build();

		// then
		assertNull(seller.getId());
		assertEquals(seller.getName(), "name");
		assertEquals(seller.getEmail(), "a@a.a");
		assertEquals(seller.getPassword(), "aa11111!");
		assertEquals(seller.getBusinessNo(), "123-45-67890");
		assertEquals(seller.getBusinessName(), "business name");
		assertEquals(seller.getStatus().toString(), "ACTIVATE");
		assertNull(seller.getCreatedAt());
		assertNull(seller.getUpdatedAt());
	}
}
