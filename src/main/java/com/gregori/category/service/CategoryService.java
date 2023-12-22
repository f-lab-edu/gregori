package com.gregori.category.service;

import java.util.List;

import com.gregori.category.domain.Category;
import com.gregori.common.exception.NotFoundException;

public interface CategoryService {

	Long saveCategory(String name);
	Long updateCategoryName(Long categoryId, String name) throws NotFoundException;
	Long deleteCategory(Long categoryId);
	Category getCategory(Long categoryId) throws NotFoundException;
	List<Category> getCategories(int limit, int offset);
}
