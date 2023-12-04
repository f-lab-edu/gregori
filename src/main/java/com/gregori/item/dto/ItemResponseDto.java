package com.gregori.item.dto;

import com.gregori.item.domain.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDto {
	private Long id;
	private String name;
	private Long price;
	private Long inventory;
	private Item.Status status;

	public ItemResponseDto toEntity(Item item) {
		return ItemResponseDto.builder()
			.id(item.getId())
			.name(item.getName())
			.price(item.getPrice())
			.inventory(item.getInventory())
			.status(item.getStatus())
			.build();
	}
}
