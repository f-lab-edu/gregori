package com.gregori.product.dto;

import com.gregori.product.domain.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

	private Long id;
	private Long sellerId;
	private String name;
	private Long price;
	private Long inventory;
	private Product.Status status;

	public ProductResponseDto toEntity(Product product) {

		return ProductResponseDto.builder()
			.id(product.getId())
			.sellerId(product.getSellerId())
			.name(product.getName())
			.price(product.getPrice())
			.inventory(product.getInventory())
			.status(product.getStatus())
			.build();
	}
}
