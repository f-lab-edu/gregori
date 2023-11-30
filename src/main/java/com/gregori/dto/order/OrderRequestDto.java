package com.gregori.dto.order;

import com.gregori.domain.order.Order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderRequestDto {
	@NotNull
	private Long memberId;

	@NotEmpty(message = "지불 방법은 필수값입니다.")
	private String paymentMethod;

	@NotEmpty(message = "지불 금액은 필수값입니다.")
	private Long paymentAmount;

	@NotEmpty(message = "배송 방법은 필수값입니다.")
	private Long deliveryCost;

	public Order toEntity() {
		return Order.builder()
			.memberId(memberId)
			.paymentAmount(paymentAmount)
			.paymentMethod(paymentMethod)
			.deliveryCost(deliveryCost)
			.build();
	}
}
