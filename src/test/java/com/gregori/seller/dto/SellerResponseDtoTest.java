package com.gregori.seller.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class SellerResponseDtoTest {

	@Test
	@DisplayName("SellerResponseDto 객체를 생성하면 private 필드를 get 메서드로 조회한다.")
	void should_getFields_when_createSellerResponseDto() {

		// given
		SellerResponseDto dto = new SellerResponseDto(1L, 1L, "111-11-11111", "일호 상점");

		// then
		assertEquals(dto.getId(), 1L);
		assertEquals(dto.getMemberId(), 1L);
		assertEquals(dto.getBusinessNumber(), "111-11-11111");
		assertEquals(dto.getBusinessName(), "일호 상점");
	}
}
