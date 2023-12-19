package com.gregori.category.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gregori.category.domain.Category;
import com.gregori.category.mapper.CategoryMapper;
import com.gregori.common.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryMapper categoryMapper;

	@Override
	public Long saveCategory(String name) {

		Category category = new Category(name);
		categoryMapper.insert(category);

		return category.getId();
	}

	@Override
	public Long updateCategory(Long categoryId, String name) {

		Category category = categoryMapper.findById(categoryId).orElseThrow(NotFoundException::new);
		category.updateCategoryName(name);
		categoryMapper.updateName(category);

		return category.getId();
	}

	@Override
	public Long deleteCategory(Long categoryId) {

		categoryMapper.deleteById(categoryId);

		return categoryId;
	}

	@Override
	public Category getCategory(Long categoryId) {

		return categoryMapper.findById(categoryId).orElseThrow(NotFoundException::new);
	}

	@Override
	public List<Category> getCategories() {

		return categoryMapper.find();
	}
}
