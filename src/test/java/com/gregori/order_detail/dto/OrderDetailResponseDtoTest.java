package com.gregori.order_detail.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gregori.order_detail.domain.OrderDetail;

import static com.gregori.order_detail.domain.OrderDetail.Status.PAYMENT_COMPLETED;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderDetailResponseDtoTest {

	@Test
	@DisplayName("OrderItemResponseDto 객체를 builder 패턴으로 생성한다.")
	void toEntityTest() {

		// given
		OrderDetail orderDetail = new OrderDetail(1L, 1L, "아이템1", 100L, 1L);

		// when
		OrderDetailResponseDto dto = new OrderDetailResponseDto().toEntity(orderDetail);

		// then
		assertEquals(orderDetail.getOrderId(), dto.getOrderId());
		assertEquals(orderDetail.getProductCount(), dto.getProductCount());
		assertEquals(orderDetail.getProductId(), dto.getProductId());
		assertEquals(orderDetail.getProductName(), dto.getProductName());
		assertEquals(orderDetail.getProductPrice(), dto.getProductPrice());
	}

	@Test
	@DisplayName("OrderItemResponseDto의 필드를 getter 메서드로 조회한다.")
	void getterTest() {

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
