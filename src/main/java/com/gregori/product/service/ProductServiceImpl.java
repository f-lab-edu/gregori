package com.gregori.product.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.common.exception.NotFoundException;
import com.gregori.product.domain.Product;
import com.gregori.product.dto.ProductCreateDto;
import com.gregori.product.dto.ProductResponseDto;
import com.gregori.product.dto.ProductUpdateDto;
import com.gregori.product.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductMapper productMapper;

	@Override
	public Long saveProduct(ProductCreateDto dto) {

		Product product = dto.toEntity();
		productMapper.insert(product);

		return product.getId();
	}

	@Override
	@Transactional
	public void updateProduct(ProductUpdateDto dto) throws NotFoundException {

		Product product = productMapper.findById(dto.getId()).orElseThrow(NotFoundException::new);
		product.updateProductInfo(dto.getCategoryId(), dto.getName(), dto.getPrice(), dto.getInventory(), dto.getStatus());
		productMapper.update(product);
	}

	@Override
	public ProductResponseDto getProduct(Long productId) {

		Product product = productMapper.findById(productId).orElseThrow(NotFoundException::new);

		return new ProductResponseDto().toEntity(product);
	}

	@Override
	public List<ProductResponseDto> getProducts(List<Long> productIds) {

		var products = productMapper.findByIds(productIds);

		return products.stream()
			.map(product -> new ProductResponseDto().toEntity(product))
			.toList();
	}
}
