package com.gregori.product.service;

import java.util.List;

import com.gregori.common.exception.NotFoundException;
import com.gregori.product.dto.ProductCreateDto;
import com.gregori.product.dto.ProductResponseDto;
import com.gregori.product.dto.ProductUpdateDto;

public interface ProductService {

	Long saveProduct(ProductCreateDto productCreateDto);
	void updateProduct(ProductUpdateDto productUpdateDto) throws NotFoundException;
	ProductResponseDto getProduct(Long productId) throws NotFoundException;
	List<ProductResponseDto> getProducts(List<Long> productIds);
}
