package com.gregori.item.domain;

import com.gregori.common.AbstractEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class Item extends AbstractEntity {
	private Long id;
	private Long sellerId;
	private String name;
	private Long price;
	private Long inventory;
	private Status status;

	@Getter
	@RequiredArgsConstructor
	public enum Status {
		PRE_SALE("준비중"), ON_SALE("판매중"), END_OF_SALE("판매종료");
		private final String description;
	}

	@Builder
	public Item(Long sellerId, String name, Long price, Long inventory) {
		this.sellerId = sellerId;
		this.name = name;
		this.price = price;
		this.inventory = inventory;
		this.status = Status.PRE_SALE;
	}

	public void updateItemInfo(String name, Long price, Long inventory) {
		this.name = name;
		this.price = price;
		this.inventory = inventory;
	}

	public void preSale() {
		this.status = Status.PRE_SALE;
	}

	public void onSale() {
		this.status = Status.ON_SALE;
	}

	public void endOfSale() {
		this.status = Status.END_OF_SALE;
	}
}
