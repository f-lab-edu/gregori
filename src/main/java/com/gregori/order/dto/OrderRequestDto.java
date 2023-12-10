package com.gregori.order.dto;

import java.util.List;

import com.gregori.order.domain.Order;
import com.gregori.order_item.dto.OrderItemRequestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderRequestDto {
	@NotNull
	private Long memberId;

	@NotBlank(message = "지불 방법은 필수값입니다.")
	private String paymentMethod;

	@NotNull(message = "지불 금액은 필수값입니다.")
	private Long paymentAmount;

	@NotNull(message = "배송 방법은 필수값입니다.")
	private Long deliveryCost;

	@NotNull(message = "주문 상품은 필수값입니다.")
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
