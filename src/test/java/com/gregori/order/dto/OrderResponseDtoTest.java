package com.gregori.order.dto;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gregori.order.domain.Order;
import com.gregori.order_item.dto.OrderItemResponseDto;

import static com.gregori.order.domain.Order.Status.ORDER_COMPLETED;
import static com.gregori.order_item.domain.OrderItem.Status.PAYMENT_COMPLETED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderResponseDtoTest {
	private final List<OrderItemResponseDto> orderItems = List.of(
		OrderItemResponseDto.builder()
			.orderId(1L)
			.orderCount(1L)
			.itemId(1L)
			.itemName("아이템1")
			.itemPrice(100L)
			.status(PAYMENT_COMPLETED)
			.build());

	@Test
	@DisplayName("Order와 OrderItems를 파라미터로 받아 OrderResponseDto 객체를 builder 패턴으로 생성한다.")
	void toEntityTest() {
		// given
		Order order = Order.builder()
			.memberId(1L)
			.paymentMethod("카드")
			.paymentAmount(1000L)
			.deliveryCost(2500L)
			.build();

		// when
		OrderResponseDto dto = new OrderResponseDto().toEntity(order, orderItems);

		// then
		assertEquals(order.getMemberId(), dto.getMemberId());
		assertEquals(order.getPaymentMethod(), dto.getPaymentMethod());
		assertEquals(order.getPaymentAmount(), dto.getPaymentAmount());
		assertEquals(order.getDeliveryCost(), dto.getDeliveryCost());
		assertNotNull(dto.getOrderItems());
		assertEquals(orderItems.get(0).getOrderId(), dto.getOrderItems().get(0).getOrderId());
		assertEquals(orderItems.get(0).getOrderCount(), dto.getOrderItems().get(0).getOrderCount());
	}

	@Test
	@DisplayName("OrderResponseDto 객체의 필드를 getter 메서드로 조회한다.")
	void getterTest() {
		// given
		OrderResponseDto dto = new OrderResponseDto(1L, 1L, "orderNo", "paymentMethod", 1L, 1L, ORDER_COMPLETED, orderItems);

		// then
		assertEquals(dto.getId(), 1L);
		assertEquals(dto.getMemberId(), 1L);
		assertEquals(dto.getOrderNumber(), "orderNo");
		assertEquals(dto.getPaymentMethod(), "paymentMethod");
		assertEquals(dto.getPaymentAmount(), 1L);
		assertEquals(dto.getDeliveryCost(), 1L);
		assertEquals(dto.getStatus(), ORDER_COMPLETED);
		assertNotNull(dto.getOrderItems());
	}
}
