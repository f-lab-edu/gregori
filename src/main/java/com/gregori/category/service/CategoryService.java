package com.gregori.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.category.domain.Category;
import com.gregori.category.mapper.CategoryMapper;
import com.gregori.common.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryMapper categoryMapper;

	public Long saveCategory(String name) {

		Category category = new Category(name);
		categoryMapper.insert(category);

		return category.getId();
	}

	@Transactional
	public void updateCategoryName(Long categoryId, String name) throws NotFoundException {

		Category category = categoryMapper.findById(categoryId).orElseThrow(NotFoundException::new);
		category.updateCategoryName(name);
		categoryMapper.updateName(category);
	}

	public void deleteCategory(Long categoryId) {

		categoryMapper.deleteById(categoryId);
	}

	public Category getCategory(Long categoryId) throws NotFoundException {

		return categoryMapper.findById(categoryId).orElseThrow(NotFoundException::new);
	}

	public List<Category> getCategories(int page) {

		int limit = 10;
		int offset = (page - 1) * limit;

		return categoryMapper.find(limit, offset);
	}
}
