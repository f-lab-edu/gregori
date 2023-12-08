package com.gregori.order_item.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gregori.order_item.domain.OrderItem;

import static com.gregori.order_item.domain.OrderItem.Status.PAYMENT_COMPLETED;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderItemResponseDtoTest {
	@Test
	@DisplayName("OrderItemResponseDto의 build 테스트")
	void toEntityTest() {
		// given
		OrderItem orderItem = new OrderItem(1L, 1L, 1L, "아이템1", 100L);

		// when
		OrderItemResponseDto dto = new OrderItemResponseDto().toEntity(orderItem);

		// then
		assertEquals(orderItem.getOrderId(), dto.getOrderId());
		assertEquals(orderItem.getOrderCount(), dto.getOrderCount());
		assertEquals(orderItem.getItemId(), dto.getItemId());
		assertEquals(orderItem.getItemName(), dto.getItemName());
		assertEquals(orderItem.getItemPrice(), dto.getItemPrice());
	}

	@Test
	@DisplayName("OrderItemResponseDto의 getter 테스트")
	void getterTest() {
		// given
		OrderItemResponseDto dto = new OrderItemResponseDto(1L, 1L, 1L, 1L, "아이템1", 100L, PAYMENT_COMPLETED);

		// then
		assertEquals(dto.getId(), 1L);
		assertEquals(dto.getOrderId(), 1L);
		assertEquals(dto.getOrderCount(), 1L);
		assertEquals(dto.getItemId(), 1L);
		assertEquals(dto.getItemName(), "아이템1");
		assertEquals(dto.getItemPrice(), 100L);
		assertEquals(dto.getStatus(), PAYMENT_COMPLETED);
	}
}
