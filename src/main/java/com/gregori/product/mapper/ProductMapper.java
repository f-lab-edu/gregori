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
	List<Product> findByIds(List<Long> productIds);
	List<Product> findBySellerId(Long sellerId);
}
