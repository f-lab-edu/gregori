package com.gregori.item.dto;

import lombok.Getter;

@Getter
public class ItemUpdateDto {
	private Long id;
	private String name;
	private Long price;
	private Long inventory;

	public ItemUpdateDto(Long id, String name, Long price, Long inventory) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.inventory = inventory;
	}
}
