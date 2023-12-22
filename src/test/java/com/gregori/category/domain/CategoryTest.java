package com.gregori.category.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

	@Test
	@DisplayName("Category 객체의 필드를 builder 패턴으로 생성하고 getter 메서드로 조회한다.")
	void should_getFields_when_buildMember() {

		// given
		Category category = Category.builder()
			.name("name")
			.build();

		// when, then
		assertNull(category.getId());
		assertEquals(category.getName(), "name");
		assertNull(category.getCreatedAt());
		assertNull(category.getUpdatedAt());
	}
}