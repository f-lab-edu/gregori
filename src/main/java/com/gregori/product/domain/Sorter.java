package com.gregori.product.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Sorter {

	CREATED_AT_DESC("최신 등록 순"),
	PRICE_DESC("가격 높은 순"),
	PRICE_ASC("가격 낮은 순");

	private final String description;
}
