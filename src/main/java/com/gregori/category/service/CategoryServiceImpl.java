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
public class CategoryServiceImpl implements CategoryService {

	private final CategoryMapper categoryMapper;

	@Override
	public Long saveCategory(String name) {

		Category category = new Category(name);
		categoryMapper.insert(category);

		return category.getId();
	}

	@Override
	public void updateCategoryName(Long categoryId, String name) throws NotFoundException {

		Category category = categoryMapper.findById(categoryId).orElseThrow(NotFoundException::new);
		category.updateCategoryName(name);
		categoryMapper.updateName(category);
	}

	@Override
	@Transactional
	public void deleteCategory(Long categoryId) {

		categoryMapper.deleteById(categoryId);
	}

	@Override
	public Category getCategory(Long categoryId) throws NotFoundException {

		return categoryMapper.findById(categoryId).orElseThrow(NotFoundException::new);
	}

	@Override
	public List<Category> getCategories(int page) {

		int limit = 10;
		int offset = (page - 1) * limit;

		return categoryMapper.find(limit, offset);
	}
}
