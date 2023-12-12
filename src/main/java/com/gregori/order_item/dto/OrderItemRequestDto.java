package com.gregori.order_item.dto;

import com.gregori.item.domain.Item;
import com.gregori.order_item.domain.OrderItem;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItemRequestDto {
	@NotNull
	private Long orderCount;

	@NotNull
	private Long itemId;

	public OrderItem toEntity(Long orderId, Item item) {
		return OrderItem.builder()
			.orderId(orderId)
			.orderCount(orderCount)
			.itemId(itemId)
			.itemName(item.getName())
			.itemPrice(item.getPrice())
			.build();
	}
}
