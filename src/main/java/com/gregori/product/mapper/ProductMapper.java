package com.gregori.product.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.gregori.product.domain.Product;

@Mapper
public interface ProductMapper {

	Long insert(Product product);
	Long update(Product product);
	void deleteById(Long productId);
	Optional<Product> findById(Long productId);
	List<Product> find(String keyword, Long categoryId, Long sellerId, Integer limit, Integer offset, String sorter);
}
