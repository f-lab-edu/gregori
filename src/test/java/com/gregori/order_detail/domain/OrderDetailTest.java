package com.gregori.order_detail.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderDetailTest {

	@Test
	@DisplayName("OrderDetail 객체의 상태를 'PAYMENT_COMPLETED'로 변경한다.")
	void paymentCompleted() {

		// given
		OrderDetail orderDetail = OrderDetail.builder()
			.orderId(1L)
			.productId(1L)
			.productName("name")
			.productPrice(1L)
			.productCount(1L)
			.build();

		orderDetail.shipmentPreparation();

		// when
		orderDetail.paymentCompleted();

		// then
		assertEquals(orderDetail.getStatus().toString(), "PAYMENT_COMPLETED");
	}

	@Test
	@DisplayName("OrderDetail 객체의 상태를 'SHIPMENT_PREPARATION'로 변경한다.")
	void shipmentPreparation() {

		// given
		OrderDetail orderDetail = OrderDetail.builder()
			.orderId(1L)
			.productId(1L)
			.productName("name")
			.productPrice(1L)
			.productCount(1L)
			.build();

		// when
		orderDetail.shipmentPreparation();

		// then
		assertEquals(orderDetail.getStatus().toString(), "SHIPMENT_PREPARATION");
	}

	@Test
	@DisplayName("OrderDetail 객체의 상태를 'SHIPPED'로 변경한다.")
	void shipped() {

		// given
		OrderDetail orderDetail = OrderDetail.builder()
			.orderId(1L)
			.productId(1L)
			.productName("name")
			.productPrice(1L)
			.productCount(1L)
			.build();

		// when
		orderDetail.shipped();

		// then
		assertEquals(orderDetail.getStatus().toString(), "SHIPPED");
	}

	@Test
	@DisplayName("OrderDetail 객체의 상태를 'DELIVERED'로 변경한다.")
	void deliveryCost() {

		// given
		OrderDetail orderDetail = OrderDetail.builder()
			.orderId(1L)
			.productId(1L)
			.productName("name")
			.productPrice(1L)
			.productCount(1L)
			.build();

		// when
		orderDetail.deliveryCost();

		// then
		assertEquals(orderDetail.getStatus().toString(), "DELIVERED");
	}

	@Test
	@DisplayName("OrderDetail 객체를 builder 패턴으로 생성하고 getter 메서드로 조회한다.")
	void builderAndGetterTest() {

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
