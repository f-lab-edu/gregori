package com.gregori.domain.order;

import com.gregori.domain.AbstractEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Order extends AbstractEntity {
	private Long id;
	private Long memberId;
	private String orderNo;
	private String paymentMethod;
	private Long paymentAmount;
	private Long deliveryCost;

	public Order(Long memberId, String orderNo, String paymentMethod, Long paymentAmount, Long deliveryCost) {
		this.memberId = memberId;
		this.orderNo = orderNo;
		this.paymentMethod = paymentMethod;
		this.paymentAmount = paymentAmount;
		this.deliveryCost = deliveryCost;
	}
}
