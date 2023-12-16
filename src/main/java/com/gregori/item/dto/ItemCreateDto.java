package com.gregori.item.dto;

import com.gregori.item.domain.Item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemCreateDto {
	@NotNull
	private Long sellerId;

	@NotBlank
	private String name;

	@NotNull
	private Long price;

	@NotNull
	private Long inventory;

	public Item toEntity() {
		return Item.builder()
			.sellerId(sellerId)
			.name(name)
			.price(price)
			.inventory(inventory)
			.build();
	}
}
