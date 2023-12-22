package com.gregori.category.domain;

import com.gregori.common.AbstractEntity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Category extends AbstractEntity {

	Long id;
	String name;

	@Builder
	public Category(String name) {
		this.name = name;
	}

	public void updateCategoryName(String name) {
		this.name = name;
	}
}
