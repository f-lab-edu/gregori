package com.gregori.category.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryTest {

	@Test
	@DisplayName("Category 객체의 이름을 수정한다.")
	void should_updateCategoryName() {

		// given
		Category category = new Category("name");

		// when
		category.updateCategoryName("new name");

		// then
		assertThat(category.getName()).isEqualTo("new name");
	}
}