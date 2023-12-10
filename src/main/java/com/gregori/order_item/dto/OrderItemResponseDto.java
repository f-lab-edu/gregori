package com.gregori.order_item.dto;

import com.gregori.order_item.domain.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDto {
	private Long id;
	private Long orderId;
	private Long orderCount;
	private Long itemId;
	private String itemName;
	private Long itemPrice;
	private OrderItem.Status status;

	public OrderItemResponseDto toEntity(OrderItem orderItem) {
		return OrderItemResponseDto.builder()
			.id(orderItem.getId())
			.orderId(orderItem.getOrderId())
			.orderCount(orderItem.getOrderCount())
			.itemId(orderItem.getItemId())
			.itemName(orderItem.getItemName())
			.itemPrice(orderItem.getItemPrice())
			.status(orderItem.getStatus())
			.build();
	}
}
