package com.gregori.product.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.gregori.product.domain.Product;

@Mapper
public interface ProductMapper {

	Long insert(Product product);
	Long update(Product product);
	void deleteByIds(List<Long> productIds);
	Optional<Product> findById(Long productId);
	List<Product> findByKeyword(String keyword, Integer limit, Integer offset, String sorter);
	List<Product> findByCategoryId(Long categoryId, Integer limit, Integer offset, String sorter);
	List<Product> findBySellerId(Long sellerId, Integer limit, Integer offset, String sorter);
}
