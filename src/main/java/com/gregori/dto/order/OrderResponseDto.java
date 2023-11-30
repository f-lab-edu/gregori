package com.gregori.dto.order;

import com.gregori.domain.order.Order;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderResponseDto {
	private Long id;
	private Long memberId;
	private String orderNo;
	private String paymentMethod;
	private Long paymentAmount;
	private Long deliveryCost;
	private Order.Status status;

	@Builder
	public OrderResponseDto(Long id, Long memberId, String orderNo, String paymentMethod, Long paymentAmount,
		Long deliveryCost, Order.Status status) {
		this.id = id;
		this.memberId = memberId;
		this.orderNo = orderNo;
		this.paymentMethod = paymentMethod;
		this.paymentAmount = paymentAmount;
		this.deliveryCost = deliveryCost;
		this.status = status;
	}

	public OrderResponseDto toEntity(Order order) {
		return OrderResponseDto.builder()
			.id(order.getId())
			.memberId(order.getMemberId())
			.orderNo(order.getOrderNo())
			.paymentMethod(order.getPaymentMethod())
			.paymentAmount(order.getPaymentAmount())
			.deliveryCost(order.getDeliveryCost())
			.status(order.getStatus())
			.build();
	}
}
