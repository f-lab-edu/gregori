package com.gregori.order.dto;

import java.util.List;

import com.gregori.order.domain.Order;
import com.gregori.order_item.dto.OrderItemRequestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderRequestDto {
	@NotNull
	private Long memberId;

	@NotBlank
	private String paymentMethod;

	@NotNull
	private Long paymentAmount;

	@NotNull
	private Long deliveryCost;

	@NotNull
	private List<OrderItemRequestDto> orderItems;

	public Order toEntity() {
		return Order.builder()
			.memberId(memberId)
			.paymentAmount(paymentAmount)
			.paymentMethod(paymentMethod)
			.deliveryCost(deliveryCost)
			.build();
	}
}
