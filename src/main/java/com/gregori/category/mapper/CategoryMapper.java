package com.gregori.category.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.gregori.category.domain.Category;

@Mapper
public interface CategoryMapper {

	Long insert(Category category);
	void updateName(Category category);
	void deleteById(Long id);
	Optional<Category> findById(Long id);
	List<Category> find(int limit, int offset);
}
