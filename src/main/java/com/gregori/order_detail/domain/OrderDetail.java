package com.gregori.order_detail.domain;

import com.gregori.common.AbstractEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderDetail extends AbstractEntity {
	private Long id;
	private Long orderId;
	private Long productId;
	private String productName;
	private Long productPrice;
	private Long productCount;
	private Status status;

	@Getter
	@RequiredArgsConstructor
	public enum Status {
		PAYMENT_COMPLETED("결제 완료"),
		SHIPMENT_PREPARATION("배송 준비중"),
		SHIPPED("배송중"),
		DELIVERED("배송 완료");
		private final String description;
	}

	@Builder
	public OrderDetail(Long orderId, Long productId, String productName, Long productPrice, Long productCount) {
		this.orderId = orderId;
		this.productId = productId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productCount = productCount;
		this.status = Status.PAYMENT_COMPLETED;
	}

	public void paymentCompleted() {
		this.status = Status.PAYMENT_COMPLETED;
	}

	public void shipmentPreparation() {
		this.status = Status.SHIPMENT_PREPARATION;
	}

	public void shipped() {
		this.status = Status.SHIPPED;
	}

	public void deliveryCost() {
		this.status = Status.DELIVERED;
	}
}
