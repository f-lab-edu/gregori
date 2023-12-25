package com.gregori.product.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gregori.product.domain.Product;
import com.gregori.product.dto.ProductCreateDto;
import com.gregori.product.dto.ProductUpdateDto;
import com.gregori.product.mapper.ProductMapper;

import static com.gregori.product.domain.Product.Status.PRE_SALE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

	@Mock
	private ProductMapper productMapper;

	@InjectMocks
	private ProductServiceImpl productService;

	@Test
	@DisplayName("상품 생성을 성공하면 id를 반환한다.")
	void should_returnId_when_saveProductSuccess() {

		// given
		ProductCreateDto dto = new ProductCreateDto(1L, "name", 1L, 1L);

		// when
		productService.saveProduct(dto);

		// then
		verify(productMapper).insert(any(Product.class));
	}

	@Test
	@DisplayName("상품 갱신을 성공하면 id를 반환한다.")
	void should_returnId_when_updateProductSuccess() {

		// given
		ProductUpdateDto dto = new ProductUpdateDto(1L, 1L, "name", 1L, 1L, PRE_SALE);

		given(productMapper.findById(1L)).willReturn(Optional.of(new Product()));

		// when
		productService.updateProduct(dto);

		// then
		verify(productMapper).update(any(Product.class));
	}

	@Test
	@DisplayName("상품 조회를 성공하면 상품을 반환한다.")
	void should_returnProduct_when_getProductSuccess() {

		// given
		Long productId = 1L;

		given(productMapper.findById(productId)).willReturn(Optional.of(new Product()));

		// when
		productService.getProduct(productId);

		// then
		verify(productMapper).findById(productId);
	}

	@Test
	@DisplayName("상품 목록을 반환한다.")
	void should_returnProducts() {

		// given
		List<Long> productIds = List.of(1L);

		given(productMapper.findByIds(productIds)).willReturn(List.of(new Product()));

		// when
		productService.getProducts(productIds);

		// then
		verify(productMapper).findByIds(productIds);
	}
}
