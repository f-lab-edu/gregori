package com.gregori.category.service;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gregori.category.domain.Category;
import com.gregori.category.mapper.CategoryMapper;
import com.gregori.common.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

	@Mock
	private CategoryMapper categoryMapper;

	@InjectMocks
	private CategoryService categoryService;

	@Test
	@DisplayName("카테고리 생성을 성공하면 id를 반환한다.")
	void should_returnId_when_saveCategorySuccess() {

		// given, when
		categoryService.saveCategory("name");

		// then
		verify(categoryMapper).insert(any(Category.class));
	}

	@Test
	@DisplayName("카테고리 갱신을 성공한다.")
	void should_updateCategorySuccess() {

		// given
		Long categoryId = 1L;
		String categoryName = "name";
		Category category = new Category(categoryName);

		given(categoryMapper.findById(1L)).willReturn(Optional.of(category));

		// when
		categoryService.updateCategoryName(categoryId, categoryName);

		// then
		verify(categoryMapper).updateName(any(Category.class));
	}

	@Test
	@DisplayName("카테고리 삭제를 성공하면 id를 반환한다.")
	void should_deleteCategorySuccess() {

		// given
		Long categoryId = 1L;

		// when
		categoryService.deleteCategory(categoryId);

		// then
		verify(categoryMapper).deleteById(categoryId);
	}

	@Test
	@DisplayName("카테고리 조회를 성공하면 카테고리를 반환한다.")
	void should_returnCategory_when_getCategorySuccess() {

		// given
		Long categoryId = 1L;
		Category category = new Category("name");

		given(categoryMapper.findById(1L)).willReturn(Optional.of(category));

		// when
		categoryService.getCategory(categoryId);

		// then
		verify(categoryMapper).findById(categoryId);

	}

	@Test
	@DisplayName("카테고리 조회를 실패하면 에러가 발생한다.")
	void should_NotFoundException_when_categoryGetFailure() {

		// given
		given(categoryMapper.findById(1L)).willReturn(Optional.empty());

		// when, then
		assertThrows(NotFoundException.class, () -> categoryService.updateCategoryName(1L, "name"));
		assertThrows(NotFoundException.class, () -> categoryService.getCategory(1L));
	}


	@Test
	@DisplayName("카테고리 목록 조회를 성공하면 카테고리 목록을 반환한다.")
	void should_returnCategories_when_getCategoriesSuccess() {

		// given, when
		categoryService.getCategories(1);

		// then
		verify(categoryMapper).find(10, 0);
	}
}
