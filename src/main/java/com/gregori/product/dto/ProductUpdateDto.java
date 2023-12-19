package com.gregori.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductUpdateDto {
	@NotNull
	private Long id;

	@NotBlank
	private String name;

	@NotNull
	private Long price;

	@NotNull
	private Long inventory;
}
