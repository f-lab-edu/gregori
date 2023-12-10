package com.gregori.order.dto;

import java.util.List;

import com.gregori.order.domain.Order;
import com.gregori.order_item.dto.OrderItemResponseDto;

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
	private String orderNo;
	private String paymentMethod;
	private Long paymentAmount;
	private Long deliveryCost;
	private Order.Status status;
	private List<OrderItemResponseDto> orderItems;

	public OrderResponseDto toEntity(Order order, List<OrderItemResponseDto> orderItems) {
		return OrderResponseDto.builder()
			.id(order.getId())
			.memberId(order.getMemberId())
			.orderNo(order.getOrderNo())
			.paymentMethod(order.getPaymentMethod())
			.paymentAmount(order.getPaymentAmount())
			.deliveryCost(order.getDeliveryCost())
			.status(order.getStatus())
			.orderItems(orderItems)
			.build();
	}
}
