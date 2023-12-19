package com.gregori.product.dto;

import com.gregori.product.domain.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductCreateDto {
	@NotNull
	private Long sellerId;

	@NotBlank
	private String name;

	@NotNull
	private Long price;

	@NotNull
	private Long inventory;

	public Product toEntity() {
		return Product.builder()
			.sellerId(sellerId)
			.name(name)
			.price(price)
			.inventory(inventory)
			.build();
	}
}
