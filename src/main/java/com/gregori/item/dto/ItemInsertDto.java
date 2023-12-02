package com.gregori.item.dto;

import lombok.Getter;

@Getter
public class ItemInsertDto {
	private String name;
	private Long price;
	private Long inventory;

	public ItemInsertDto(String name, Long price, Long inventory) {
		this.name = name;
		this.price = price;
		this.inventory = inventory;
	}
}
