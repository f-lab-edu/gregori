package com.gregori.order_detail.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.gregori.order_detail.domain.OrderDetail.Status.PAYMENT_COMPLETED;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderDetailResponseDtoTest {

	@Test
	@DisplayName("OrderDetailResponseDto 객체를 생성하면 get 메서드로 필드를 조회한다.")
	void should_getFields_when_createOrderDetailResponseDto() {

		// given
		OrderDetailResponseDto dto = new OrderDetailResponseDto(1L, 1L, 1L, "아이템1", 100L, 1L, PAYMENT_COMPLETED);

		// then
		assertEquals(dto.getId(), 1L);
		assertEquals(dto.getOrderId(), 1L);
		assertEquals(dto.getProductId(), 1L);
		assertEquals(dto.getProductName(), "아이템1");
		assertEquals(dto.getProductPrice(), 100L);
		assertEquals(dto.getProductCount(), 1L);
		assertEquals(dto.getStatus(), PAYMENT_COMPLETED);
	}
}
