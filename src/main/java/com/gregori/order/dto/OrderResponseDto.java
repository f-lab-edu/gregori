package com.gregori.order.dto;

import java.util.List;

import com.gregori.order.domain.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

	private Long id;
	private Long memberId;
	private String orderNumber;
	private String paymentMethod;
	private Long paymentAmount;
	private Long deliveryCost;
	private Order.Status status;
	private List<OrderDetailResponseDto> orderDetails;

	public OrderResponseDto toEntity(Order order, List<OrderDetailResponseDto> orderDetails) {

		return OrderResponseDto.builder()
			.id(order.getId())
			.memberId(order.getMemberId())
			.orderNumber(order.getOrderNumber())
			.paymentMethod(order.getPaymentMethod())
			.paymentAmount(order.getPaymentAmount())
			.deliveryCost(order.getDeliveryCost())
			.status(order.getStatus())
			.orderDetails(orderDetails)
			.build();
	}
}
