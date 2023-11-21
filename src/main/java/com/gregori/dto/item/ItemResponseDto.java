package com.gregori.dto.item;

import com.gregori.domain.item.Item;

import lombok.Builder;

public class ItemResponseDto {
	private Long id;
	private String name;
	private Long price;
	private Long inventory;
	private Item.Status status;

	@Builder
	public ItemResponseDto(Long id, String name, Long price, Long inventory, Item.Status status) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.inventory = inventory;
		this.status = status;
	}
}
