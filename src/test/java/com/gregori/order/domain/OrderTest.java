package com.gregori.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderTest {

	@Test
	@DisplayName("15자리 이상의 주문 번호를 생성한다.")
	void orderNumberGenerator() {

		// given
		Order order = Order.builder()
			.memberId(1L)
			.paymentMethod("paymentMethod")
			.paymentAmount(1L)
			.deliveryCost(1L)
			.build();

		// then
		assertNotNull(order.getOrderNumber());
		assertTrue(order.getOrderNumber().startsWith("ORD_"));
		assertTrue(order.getOrderNumber().length() > 15);
	}

	@Test
	@DisplayName("Order 객체의 상태를 'ORDER_COMPLETED'로 변경한다.")
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
	@DisplayName("Order 객체의 상태를 'ORDER_CANCELLED'로 변경한다.")
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
	@DisplayName("Order 객체를 builder 패턴으로 생성하고 getter 메서드로 조회한다.")
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
