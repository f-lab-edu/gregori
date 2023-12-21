package com.gregori.category.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.gregori.category.domain.Category;

@Mapper
public interface CategoryMapper {

	Long insert(Category category);
	Long updateName(Category category);
	Long deleteById(Long categoryId);
	Optional<Category> findById(Long categoryId);
	List<Category> find(int limit, int offset);
}
