package com.gregori.order.dto;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gregori.order.domain.Order;
import com.gregori.order_detail.dto.OrderDetailResponseDto;

import static com.gregori.order_detail.domain.OrderDetail.Status.PAYMENT_COMPLETED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderResponseDtoTest {

	private final List<OrderDetailResponseDto> orderDetails = List.of(
		OrderDetailResponseDto.builder()
			.orderId(1L)
			.productId(1L)
			.productName("아이템1")
			.productPrice(100L)
			.productCount(1L)
			.status(PAYMENT_COMPLETED)
			.build());

	@Test
	@DisplayName("OrderResponseDto 객체를 생성하면 private 필드를 get 메서드로 조회한다.")
	void should_getFields_when_createOrderResponseDto() {

		// given
		Order order = Order.builder()
			.memberId(1L)
			.paymentMethod("카드")
			.paymentAmount(1000L)
			.deliveryCost(2500L)
			.build();

		// when
		OrderResponseDto dto = new OrderResponseDto().toEntity(order, orderDetails);

		// then
		assertEquals(order.getMemberId(), dto.getMemberId());
		assertEquals(order.getPaymentMethod(), dto.getPaymentMethod());
		assertEquals(order.getPaymentAmount(), dto.getPaymentAmount());
		assertEquals(order.getDeliveryCost(), dto.getDeliveryCost());
		assertNotNull(dto.getOrderDetails());
		assertEquals(orderDetails.get(0).getOrderId(), dto.getOrderDetails().get(0).getOrderId());
		assertEquals(orderDetails.get(0).getProductCount(), dto.getOrderDetails().get(0).getProductCount());
	}
}
