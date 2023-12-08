package com.gregori.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderTest {
	@Test
	@DisplayName("주문 번호를 생성하는 메서드")
	void orderNoGenerator() {
		// given
		Order order = Order.builder()
			.memberId(1L)
			.paymentMethod("paymentMethod")
			.paymentAmount(1L)
			.deliveryCost(1L)
			.build();

		// then
		assertNotNull(order.getOrderNo());
		assertTrue(order.getOrderNo().startsWith("ORD_"));
		assertTrue(order.getOrderNo().length() > 15);
	}

	@Test
	@DisplayName("주문 상태 주문 완료 메서드")
	void orderCompleted() {
		// given
		Order order = Order.builder()
			.memberId(1L)
			.paymentMethod("paymentMethod")
			.paymentAmount(1L)
			.deliveryCost(1L)
			.build();

		// when
		order.orderCompleted();

		// then
		assertEquals(order.getStatus().toString(), "ORDER_COMPLETED");
	}

	@Test
	@DisplayName("주문 상태 주문 취소 메서드")
	void orderCancelled() {
		// given
		Order order = Order.builder()
			.memberId(1L)
			.paymentMethod("paymentMethod")
			.paymentAmount(1L)
			.deliveryCost(1L)
			.build();

		// when
		order.orderCancelled();

		// then
		assertEquals(order.getStatus().toString(), "ORDER_CANCELLED");
	}

	@Test
	@DisplayName("Order 도메인의 builer와 getter 테스트")
	void builderAndGetterTest() {
		// given
		Order order = Order.builder()
			.memberId(1L)
			.paymentMethod("paymentMethod")
			.paymentAmount(1L)
			.deliveryCost(1L)
			.build();

		// then
		assertNull(order.getId());
		assertEquals(order.getMemberId(), 1L);
		assertEquals(order.getPaymentMethod(), "paymentMethod");
		assertEquals(order.getPaymentAmount(), 1L);
		assertEquals(order.getDeliveryCost(), 1L);
		assertEquals(order.getStatus().toString(), "ORDER_COMPLETED");
		assertNull(order.getCreatedAt());
		assertNull(order.getUpdatedAt());
	}
}
