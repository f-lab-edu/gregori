package com.gregori.product.service;

import java.util.List;

import com.gregori.common.exception.NotFoundException;
import com.gregori.product.domain.Product;
import com.gregori.product.dto.ProductCreateDto;
import com.gregori.product.dto.ProductResponseDto;
import com.gregori.product.dto.ProductUpdateDto;

public interface ProductService {

	Long saveProduct(ProductCreateDto productCreateDto);
	Long updateProduct(ProductUpdateDto productUpdateDto) throws NotFoundException;
	Long updateProductStatus(Product.Status status, Long productId) throws NotFoundException;
	ProductResponseDto getProduct(Long productId) throws NotFoundException;
	List<ProductResponseDto> getProducts(List<Long> productIds);
}
