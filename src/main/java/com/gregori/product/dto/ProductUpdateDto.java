package com.gregori.product.dto;

import com.gregori.product.domain.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductUpdateDto {

	@NotNull
	private Long id;

	@NotNull
	private Long categoryId;

	@NotBlank
	private String name;

	@NotNull
	private Long price;

	@NotNull
	private Long inventory;

	@NotNull
	private Product.Status status;
}
