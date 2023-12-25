package com.gregori.product.domain;

import com.gregori.common.AbstractEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class Product extends AbstractEntity {

	private Long id;
	private Long sellerId;
	private Long categoryId;
	private String name;
	private Long price;
	private Long inventory;
	private Status status;

	@Getter
	@RequiredArgsConstructor
	public enum Status {

		PRE_SALE("준비중"),
		ON_SALE("판매중"),
		END_OF_SALE("판매종료");

		private final String description;
	}

	@Builder
	public Product(Long sellerId, Long categoryId, String name, Long price, Long inventory) {

		this.sellerId = sellerId;
		this.categoryId = categoryId;
		this.name = name;
		this.price = price;
		this.inventory = inventory;
		this.status = Status.PRE_SALE;
	}

	public void updateProductInfo(Long categoryId, String name, Long price, Long inventory, Status status) {

		this.categoryId = categoryId;
		this.name = name;
		this.price = price;
		this.inventory = inventory;
		this.status = status;
	}
}
