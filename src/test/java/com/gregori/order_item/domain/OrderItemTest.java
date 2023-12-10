package com.gregori.order_item.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderItemTest {
	@Test
	@DisplayName("OrderItem 객체의 상태를 'PAYMENT_COMPLETED'로 변경한다.")
	void paymentCompleted() {
		// given
		OrderItem orderItem = OrderItem.builder()
			.orderId(1L)
			.orderCount(1L)
			.itemId(1L)
			.itemName("name")
			.itemPrice(1L)
			.build();

		orderItem.shipmentPreparation();

		// when
		orderItem.paymentCompleted();

		// then
		assertEquals(orderItem.getStatus().toString(), "PAYMENT_COMPLETED");
	}

	@Test
	@DisplayName("OrderItem 객체의 상태를 'SHIPMENT_PREPARATION'로 변경한다.")
	void shipmentPreparation() {
		// given
		OrderItem orderItem = OrderItem.builder()
			.orderId(1L)
			.orderCount(1L)
			.itemId(1L)
			.itemName("name")
			.itemPrice(1L)
			.build();

		// when
		orderItem.shipmentPreparation();

		// then
		assertEquals(orderItem.getStatus().toString(), "SHIPMENT_PREPARATION");
	}

	@Test
	@DisplayName("OrderItem 객체의 상태를 'SHIPPED'로 변경한다.")
	void shipped() {
		// given
		OrderItem orderItem = OrderItem.builder()
			.orderId(1L)
			.orderCount(1L)
			.itemId(1L)
			.itemName("name")
			.itemPrice(1L)
			.build();

		// when
		orderItem.shipped();

		// then
		assertEquals(orderItem.getStatus().toString(), "SHIPPED");
	}

	@Test
	@DisplayName("OrderItem 객체의 상태를 'DELIVERED'로 변경한다.")
	void deliveryCost() {
		// given
		OrderItem orderItem = OrderItem.builder()
			.orderId(1L)
			.orderCount(1L)
			.itemId(1L)
			.itemName("name")
			.itemPrice(1L)
			.build();

		// when
		orderItem.deliveryCost();

		// then
		assertEquals(orderItem.getStatus().toString(), "DELIVERED");
	}

	@Test
	@DisplayName("OrderItem 객체를 builder 패턴으로 생성하고 getter 메서드로 조회한다.")
	void builderAndGetterTest() {
		// given
		OrderItem orderItem = OrderItem.builder()
			.orderId(1L)
			.orderCount(1L)
			.itemId(1L)
			.itemName("name")
			.itemPrice(1L)
			.build();

		// then
		assertNull(orderItem.getId());
		assertEquals(orderItem.getOrderId(), 1L);
		assertEquals(orderItem.getOrderCount(), 1L);
		assertEquals(orderItem.getItemName(), "name");
		assertEquals(orderItem.getItemPrice(), 1L);
		assertEquals(orderItem.getStatus().toString(), "PAYMENT_COMPLETED");
		assertNull(orderItem.getCreatedAt());
		assertNull(orderItem.getUpdatedAt());
	}
}
