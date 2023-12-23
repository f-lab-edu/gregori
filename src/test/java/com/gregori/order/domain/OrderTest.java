package com.gregori.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.gregori.order.domain.Order.Status.ORDER_CANCELLED;
import static com.gregori.order.domain.Order.Status.ORDER_COMPLETED;
import static com.gregori.order.domain.Order.Status.ORDER_PROCESSING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderTest {

	@Test
	@DisplayName("15자리 이상의 주문 번호를 생성한다.")
	void should_generateOrderNumber() {

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
	@DisplayName("Order 객체의 상태를 'ORDER_CANCELLED'로 변경한다.")
	void should_orderCancelled() {

		// given
		Order order = new Order(1L, "method", 1L, 1L);
		Order.Status status = order.getStatus();


		// when
		order.orderCancelled();
		Order.Status result = order.getStatus();

		// then
		assertEquals(status, ORDER_PROCESSING);
		assertEquals(result, ORDER_CANCELLED);
	}

	@Test
	@DisplayName("Order 객체의 상태를 'ORDER_PROCESSING'로 변경한다.")
	void should_orderProcessing() {

		// given
		Order order = new Order(1L, "method", 1L, 1L);
		order.orderCompleted();
		Order.Status status = order.getStatus();

		// when
		order.orderProcessing();
		Order.Status result = order.getStatus();

		// then
		assertEquals(status, ORDER_COMPLETED);
		assertEquals(result, ORDER_PROCESSING);
	}

	@Test
	@DisplayName("Order 객체의 상태를 'ORDER_CANCELLED'로 변경한다.")
	void should_orderCompleted() {

		// given
		Order order = new Order(1L, "method", 1L, 1L);
		Order.Status status = order.getStatus();


		// when
		order.orderCompleted();
		Order.Status result = order.getStatus();

		// then
		assertEquals(status, ORDER_PROCESSING);
		assertEquals(result, ORDER_COMPLETED);
	}
}
