package com.gregori.category.mapper;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gregori.category.domain.Category;
import com.gregori.common.CustomMybatisTest;

import static org.assertj.core.api.Assertions.assertThat;

@CustomMybatisTest
class CategoryMapperTest {

	@Autowired
	private CategoryMapper categoryMapper;

	List<Long> categoryIds = new CopyOnWriteArrayList<>();

	@AfterEach
	void afterEach() {
		if (!categoryIds.isEmpty()) {
			categoryIds.forEach(categoryId -> categoryMapper.deleteById(categoryId));
			categoryIds.clear();
		}
	}

	@Test
	@DisplayName("새로운 카테고리를 추가한다.")
	void should_insert() {

		// given
		Category category = new Category("name");

		// when
		categoryMapper.insert(category);
		categoryIds.add(category.getId());
		Optional<Category> result = categoryMapper.findById(category.getId());

		// then
		assertThat(category.getId()).isNotNull();
		assertThat(result.isPresent()).isTrue();
	}

	@Test
	@DisplayName("카테고리 이름을 갱신한다.")
	void should_updateName() {

		// given
		Category category = new Category("name");
		categoryMapper.insert(category);
		categoryIds.add(category.getId());
		category.updateCategoryName("new name");

		// when
		categoryMapper.updateName(category);

		// then
		Optional<Category> result = categoryMapper.findById(category.getId());
		assertThat(result.isPresent()).isTrue();
		assertThat(result.get().getName()).isEqualTo("new name");
	}

	@Test
	@DisplayName("id로 카테고리를 삭제한다.")
	void should_deleteById() {

		// given
		Category category = new Category("name");
		categoryMapper.insert(category);

		// when
		categoryMapper.deleteById(category.getId());

		// then
		Optional<Category> result = categoryMapper.findById(category.getId());
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("id로 카테고리를 조회한다.")
	void should_findById() {

		// given
		Category category = new Category("name");
		categoryMapper.insert(category);

		// when
		Optional<Category> result = categoryMapper.findById(category.getId());

		// then
		assertThat(result.isPresent()).isTrue();
		assertThat(result.get().getId()).isEqualTo(category.getId());
	}

	@Test
	@DisplayName("전체 카테고리를 조회한다.")
	void should_find() {

		// given
		Category category1 = new Category("name1");
		Category category2 = new Category("name2");
		Category category3 = new Category("name3");

		categoryMapper.insert(category1);
		categoryMapper.insert(category2);
		categoryMapper.insert(category3);

		categoryIds.add(category1.getId());
		categoryIds.add(category2.getId());
		categoryIds.add(category3.getId());

		// when
		List<Category> result = categoryMapper.find(10, 0);

		// then
		assertThat(result.size()).isEqualTo(3);
	}
}
