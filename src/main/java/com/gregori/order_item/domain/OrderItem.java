package com.gregori.order_item.domain;

import com.gregori.common.AbstractEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItem extends AbstractEntity {
	private Long id;
	private Long orderId;
	private Long orderCount;
	private Long itemId;
	private String itemName;
	private Long itemPrice;
	private Status status;

	@Getter
	@RequiredArgsConstructor
	public enum Status {
		PAYMENT_COMPLETED("결제 완료"), SHIPMENT_PREPARATION("배송 준비중"),
		SHIPPED("배송중"), DELIVERED("배송 완료");
		private final String description;
	}

	@Builder
	public OrderItem(Long orderId, Long orderCount, Long itemId, String itemName, Long itemPrice) {
		this.orderId = orderId;
		this.orderCount = orderCount;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemPrice = itemPrice;
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
