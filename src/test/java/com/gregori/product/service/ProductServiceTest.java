package com.gregori.product.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gregori.common.exception.BusinessRuleViolationException;
import com.gregori.common.exception.NotFoundException;
import com.gregori.common.exception.ValidationException;
import com.gregori.order.domain.OrderDetail;
import com.gregori.order.mapper.OrderDetailMapper;
import com.gregori.product.domain.Product;
import com.gregori.product.domain.Sorter;
import com.gregori.product.dto.ProductCreateDto;
import com.gregori.product.dto.ProductUpdateDto;
import com.gregori.product.mapper.ProductMapper;
import com.gregori.seller.domain.Seller;
import com.gregori.seller.mapper.SellerMapper;

import static com.gregori.common.domain.IsDeleted.TRUE;
import static com.gregori.product.domain.Product.Status.PRE_SALE;
import static com.gregori.product.domain.Sorter.CREATED_AT_DESC;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private SellerMapper sellerMapper;

	@Mock
	private ProductMapper productMapper;

	@Mock
	private OrderDetailMapper orderDetailMapper;

	@InjectMocks
	private ProductService productService;

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
	@DisplayName("상품 갱신을 성공한다.")
	void should_updateProductSuccess() {

		// given
		ProductUpdateDto dto = new ProductUpdateDto(1L, 1L, "name", 1L, 1L, PRE_SALE);
		Seller seller = new Seller(1L, "123-45-67891", "name");

		given(sellerMapper.findById(null)).willReturn(Optional.of(seller));
		given(productMapper.findById(1L)).willReturn(Optional.of(new Product()));

		// when
		productService.updateProduct(1L, dto);

		// then
		verify(productMapper).update(any(Product.class));
	}

	@Test
	@DisplayName("잘못된 입력값이면 상품 생성과 갱신을 실패한다.")
	void should_ValidationException_when_invalidPriceAndInventory() {

		// given
		ProductCreateDto dto1 = new ProductCreateDto(1L, "name", -1L, 1L);
		ProductUpdateDto dto2 = new ProductUpdateDto(1L, 1L, "name", 1L, -1L, PRE_SALE);

		// when, then
		assertThrows(ValidationException.class, () -> productService.saveProduct(dto1));
		assertThrows(ValidationException.class, () -> productService.updateProduct(1L, dto2));
	}

	@Test
	@DisplayName("상품 삭제를 성공한다.")
	void should_deleteProductSuccess() {

		// given
		Long productId = 1L;
		Seller seller = new Seller(1L, "123-45-67891", "name");

		given(sellerMapper.findById(null)).willReturn(Optional.of(seller));
		given(productMapper.findById(productId)).willReturn(Optional.of(new Product()));
		given(orderDetailMapper.findByProductId(productId)).willReturn(List.of());

		// when
		productService.deleteProduct(1L, productId);

		// then
		verify(productMapper).updateIsDeleted(productId, TRUE);
	}

	@Test
	@DisplayName("주문 상품의 배송이 완료되지 않았으면 상품 삭제를 실패한다.")
	void should_BusinessRuleViolationException_when_orderDetailIsDeliveredFalse() {

		// given
		Long productId = 1L;
		OrderDetail orderDetail = new OrderDetail(1L, 1L, 1L, "name", 1L, 1L);
		Seller seller = new Seller(1L, "123-45-67891", "name");

		given(sellerMapper.findById(null)).willReturn(Optional.of(seller));
		given(productMapper.findById(productId)).willReturn(Optional.of(new Product()));
		given(orderDetailMapper.findByProductId(productId)).willReturn(List.of(orderDetail));

		// when, then
		assertThrows(BusinessRuleViolationException.class, () -> productService.deleteProduct(1L, productId));
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
	@DisplayName("상품 조회를 실패하면 에러가 발생한다.")
	void should_NotFoundException_when_findProductFailure() {

		// given
		ProductUpdateDto dto = new ProductUpdateDto(1L, 1L, "name", 1L, 1L, PRE_SALE);

		given(productMapper.findById(1L)).willReturn(Optional.empty());

		// when, then
		assertThrows(NotFoundException.class, () -> productService.updateProduct(1L, dto));
		assertThrows(NotFoundException.class, () -> productService.deleteProduct(1L, 1L));
		assertThrows(NotFoundException.class, () -> productService.getProduct(1L));
	}

	@Test
	@DisplayName("상품 목록 조회를 성공하면 상품 목록을 반환한다.")
	void should_returnProducts_when_getProductsSuccess() {

		// given
		String keyword = "keyword";
		int page = 1;
		Sorter sorter = CREATED_AT_DESC;

		// when
		productService.getProducts(keyword, null, null, page, sorter);

		// then
		verify(productMapper).find(keyword,  null, null,10, 0, sorter.toString());
	}
}
