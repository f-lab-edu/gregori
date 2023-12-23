package com.gregori.order_detail.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.gregori.order_detail.domain.OrderDetail.Status.DELIVERED;
import static com.gregori.order_detail.domain.OrderDetail.Status.PAYMENT_COMPLETED;
import static com.gregori.order_detail.domain.OrderDetail.Status.SHIPMENT_PREPARATION;
import static com.gregori.order_detail.domain.OrderDetail.Status.SHIPPED;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderDetailTest {

	@Test
	@DisplayName("OrderDetail 객체의 상태를 'PAYMENT_COMPLETED'로 변경한다.")
	void should_paymentCompleted() {

		// given
		OrderDetail orderDetail = new OrderDetail(1L, 1L, "name", 1L, 1L);
		orderDetail.shipmentPreparation();
		OrderDetail.Status status = orderDetail.getStatus();

		// when
		orderDetail.paymentCompleted();

		// then
		assertEquals(status, SHIPMENT_PREPARATION);
		assertEquals(orderDetail.getStatus(), PAYMENT_COMPLETED);
	}

	@Test
	@DisplayName("OrderDetail 객체의 상태를 'SHIPMENT_PREPARATION'로 변경한다.")
	void should_shipmentPreparation() {

		// given
		OrderDetail orderDetail = new OrderDetail(1L, 1L, "name", 1L, 1L);
		OrderDetail.Status status = orderDetail.getStatus();

		// when
		orderDetail.shipmentPreparation();

		// then
		assertEquals(status, PAYMENT_COMPLETED);
		assertEquals(orderDetail.getStatus(), SHIPMENT_PREPARATION);
	}

	@Test
	@DisplayName("OrderDetail 객체의 상태를 'SHIPPED'로 변경한다.")
	void should_shipped() {

		// given
		OrderDetail orderDetail = new OrderDetail(1L, 1L, "name", 1L, 1L);
		OrderDetail.Status status = orderDetail.getStatus();

		// when
		orderDetail.shipped();

		// then
		assertEquals(status, PAYMENT_COMPLETED);
		assertEquals(orderDetail.getStatus(), SHIPPED);
	}

	@Test
	@DisplayName("OrderDetail 객체의 상태를 'DELIVERED'로 변경한다.")
	void should_deliveryCost() {

		// given
		OrderDetail orderDetail = new OrderDetail(1L, 1L, "name", 1L, 1L);
		OrderDetail.Status status = orderDetail.getStatus();

		// when
		orderDetail.deliveryCost();

		// then
		assertEquals(status, PAYMENT_COMPLETED);
		assertEquals(orderDetail.getStatus(), DELIVERED);
	}
}
