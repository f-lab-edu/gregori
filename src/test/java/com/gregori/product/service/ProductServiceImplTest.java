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
	@DisplayName("새로운 상품을 저장하고 id를 반환한다.")
	void should_saveProduct() {

		// given
		ProductCreateDto dto = new ProductCreateDto(1L, "name", 1L, 1L);

		// when
		productService.saveProduct(dto);

		// then
		verify(productMapper).insert(any(Product.class));
	}

	@Test
	@DisplayName("DB에 저장된 상품을 수정하고 id를 반환한다.")
	void should_updateProduct() {

		// given
		ProductUpdateDto dto = new ProductUpdateDto(1L, "name", 1L, 1L);

		given(productMapper.findById(1L)).willReturn(Optional.of(new Product()));

		// when
		productService.updateProduct(dto);

		// then
		verify(productMapper).update(any(Product.class));
	}

	@Test
	@DisplayName("DB에 저장된 상품의 상태를 변경하고 id를 반환한다.")
	void should_updateItemStatus() {

		// given
		Long productId = 1L;

		given(productMapper.findById(productId)).willReturn(Optional.of(new Product()));

		// when
		productService.updateProductStatus(PRE_SALE, productId);

		// then
		verify(productMapper).update(any(Product.class));
	}

	@Test
	@DisplayName("상품의 id로 DB에 저장된 상품을 조회해서 반환한다.")
	void should_getProduct() {

		// given
		Long productId = 1L;

		given(productMapper.findById(productId)).willReturn(Optional.of(new Product()));

		// when
		productService.getProduct(productId);

		// then
		verify(productMapper).findById(productId);
	}

	@Test
	@DisplayName("상품 id 목록으로 DB에 저장된 상품을 전부 조회해서 반환한다.")
	void should_getProducts() {

		// given
		List<Long> productIds = List.of(1L);

		given(productMapper.findByIds(productIds)).willReturn(List.of(new Product()));

		// when
		productService.getProducts(productIds);

		// then
		verify(productMapper).findByIds(productIds);
	}
}
