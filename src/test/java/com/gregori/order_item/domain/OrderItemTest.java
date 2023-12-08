package com.gregori.order_item.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderItemTest {
	@Test
	@DisplayName("주문상품 상태 결제 완료 메서드")
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
	@DisplayName("주문상품 상태 배송 준비중 메서드")
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
	@DisplayName("주문상품 상태 배송중 메서드")
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
	@DisplayName("주문상품 상태 배송 완료 메서드")
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
	@DisplayName("OrderItem 도메인의 builer와 getter 테스트")
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
