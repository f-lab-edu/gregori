package com.gregori.order.domain;

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
	private Long productSellerId;
	private String productName;
	private Long productPrice;
	private Long productCount;
	private Status status;

	@Getter
	@RequiredArgsConstructor
	public enum Status {

		PAYMENT_CANCELED("결제 취소"),
		PAYMENT_COMPLETED("결제 완료"),
		SHIPMENT_PREPARATION("배송 준비중"),
		SHIPPED("배송중"),
		DELIVERED("배송 완료");

		private final String description;
	}

	@Builder
	public OrderDetail(Long orderId, Long productId, Long productSellerId, String productName, Long productPrice, Long productCount) {
		this.orderId = orderId;
		this.productId = productId;
		this.productSellerId = productSellerId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productCount = productCount;
		this.status = Status.PAYMENT_COMPLETED;
	}

	public void paymentCanceled() {
		this.status = Status.PAYMENT_CANCELED;
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

	public void delivered() {
		this.status = Status.DELIVERED;
	}
}
