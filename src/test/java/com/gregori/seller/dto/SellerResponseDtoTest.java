package com.gregori.seller.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.gregori.seller.domain.Seller;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class SellerResponseDtoTest {
	@Test
	@DisplayName("Seller를 파라미터로 받아 SellerResposeDto 객체를 builder 패턴으로 생성한다.")
	void toEntityTest() {

		// given
		Seller seller = Seller.builder()
			.memberId(1L)
			.businessNumber("111-11-11111")
			.businessName("일호 상점")
			.build();

		// when
		SellerResponseDto dto = new SellerResponseDto().toEntity(seller);

		// then
		assertEquals(seller.getId(), dto.getId());
		assertEquals(seller.getMemberId(), dto.getMemberId());
		assertEquals(seller.getBusinessNumber(), dto.getBusinessNumber());
		assertEquals(seller.getBusinessName(), dto.getBusinessName());
	}

	@Test
	@DisplayName("SellerResponseDto 객체의 필드를 getter 메서드로 조회한다.")
	void getterTest() {

		// given
		SellerResponseDto dto = new SellerResponseDto(1L, 1L, "111-11-11111", "일호 상점");

		// then
		assertEquals(dto.getId(), 1L);
		assertEquals(dto.getMemberId(), 1L);
		assertEquals(dto.getBusinessNumber(), "111-11-11111");
		assertEquals(dto.getBusinessName(), "일호 상점");
	}
}
