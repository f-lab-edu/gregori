package com.gregori.product.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.gregori.common.domain.IsDeleted;
import com.gregori.product.domain.Product;

@Mapper
public interface ProductMapper {

	Long insert(Product product);
	void update(Product product);
	void updateInventory(Long id, Long inventory);
	void updateIsDeleted(Long id, IsDeleted isDeleted);
	void deleteById(Long id);
	Optional<Product> findById(Long id);
	List<Product> find(String keyword, Long categoryId, Long sellerId, Integer limit, Integer offset, String sorter);
}
