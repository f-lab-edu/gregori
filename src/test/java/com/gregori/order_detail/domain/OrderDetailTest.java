package com.gregori.order_detail.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.gregori.order_detail.domain.OrderDetail.Status.DELIVERED;
import static com.gregori.order_detail.domain.OrderDetail.Status.PAYMENT_COMPLETED;
import static com.gregori.order_detail.domain.OrderDetail.Status.SHIPMENT_PREPARATION;
import static com.gregori.order_detail.domain.OrderDetail.Status.SHIPPED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

	@Test
	@DisplayName("OrderDetail 객체의 필드를 builder 패턴으로 생성하고 getter 메서드로 조회한다.")
	void should_getFields_when_buildOrderDetail() {

		// given
		OrderDetail orderDetail = OrderDetail.builder()
			.orderId(1L)
			.productId(1L)
			.productName("name")
			.productPrice(1L)
			.productCount(1L)
			.build();

		// then
		assertNull(orderDetail.getId());
		assertEquals(orderDetail.getOrderId(), 1L);
		assertEquals(orderDetail.getProductName(), "name");
		assertEquals(orderDetail.getProductPrice(), 1L);
		assertEquals(orderDetail.getProductCount(), 1L);
		assertEquals(orderDetail.getStatus().toString(), "PAYMENT_COMPLETED");
		assertNull(orderDetail.getCreatedAt());
		assertNull(orderDetail.getUpdatedAt());
	}
}
