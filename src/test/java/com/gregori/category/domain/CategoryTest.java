package com.gregori.category.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryTest {

	@Test
	@DisplayName("Category 객체의 이름을 수정한다.")
	void should_updateCategoryName() {

		// given
		Category category = new Category("name");

		// when
		category.updateCategoryName("new name");

		// then
		assertEquals(category.getName(), "new name");
	}
}