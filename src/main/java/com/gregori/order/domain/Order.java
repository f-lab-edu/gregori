package com.gregori.order.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import com.gregori.common.AbstractEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class Order extends AbstractEntity {
	private Long id;
	private Long memberId;
	private String orderNumber;
	private String paymentMethod;
	private Long paymentAmount;
	private Long deliveryCost;
	private Status status;

	@Getter
	@RequiredArgsConstructor
	public enum Status {
		ORDER_COMPLETED("주문 완료"), ORDER_CANCELLED("주문 취소");
		private final String description;
	}

	@Builder
	public Order(Long memberId, String paymentMethod, Long paymentAmount, Long deliveryCost) {
		this.memberId = memberId;
		this.orderNumber = orderNumberGenerator();
		this.paymentMethod = paymentMethod;
		this.paymentAmount = paymentAmount;
		this.deliveryCost = deliveryCost;
		this.status = Status.ORDER_COMPLETED;
	}

	private String orderNumberGenerator() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String datetime = LocalDateTime.now().format(formatter);

		Random random = new Random();
		int randomNumber = random.nextInt(9000) + 1000;

		return "ORD_" + datetime + memberId + randomNumber;
	}

	public void orderCompleted() {
		this.status = Status.ORDER_COMPLETED;
	}

	public void orderCancelled() {
		this.status = Status.ORDER_CANCELLED;
	}
}
