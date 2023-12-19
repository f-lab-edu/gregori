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

import static com.gregori.product.domain.Product.Status.END_OF_SALE;
import static com.gregori.product.domain.Product.Status.ON_SALE;
import static com.gregori.product.domain.Product.Status.PRE_SALE;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductMapper productMapper;

	@Override
	public Long saveProduct(ProductCreateDto productCreateDto) {

		Product product = productCreateDto.toEntity();
		productMapper.insert(product);

		return product.getId();
	}

	@Override
	@Transactional
	public Long updateProduct(ProductUpdateDto productUpdateDto) throws NotFoundException {

		Product product = productMapper.findById(productUpdateDto.getId()).orElseThrow(NotFoundException::new);
		product.updateProductInfo(productUpdateDto.getName(), productUpdateDto.getPrice(),
			productUpdateDto.getInventory());
		productMapper.update(product);

		return product.getId();
	}

	@Override
	@Transactional
	public Long updateProductStatus(Product.Status status, Long productId) throws NotFoundException {

		Product product = productMapper.findById(productId).orElseThrow(NotFoundException::new);

		if (status == PRE_SALE) {
			product.preSale();
		} else if (status == ON_SALE) {
			product.onSale();
		} else if (status == END_OF_SALE) {
			product.endOfSale();
		}

		productMapper.update(product);

		return product.getId();
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
