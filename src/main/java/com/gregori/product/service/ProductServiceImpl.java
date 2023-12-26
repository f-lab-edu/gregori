package com.gregori.product.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.common.exception.BusinessRuleViolationException;
import com.gregori.common.exception.NotFoundException;
import com.gregori.common.exception.ValidationException;
import com.gregori.order_detail.domain.OrderDetail;
import com.gregori.order_detail.mapper.OrderDetailMapper;
import com.gregori.product.domain.Product;
import com.gregori.product.domain.Sorter;
import com.gregori.product.dto.ProductCreateDto;
import com.gregori.product.dto.ProductResponseDto;
import com.gregori.product.dto.ProductUpdateDto;
import com.gregori.product.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;

import static com.gregori.common.domain.IsDeleted.TRUE;
import static com.gregori.order_detail.domain.OrderDetail.Status.DELIVERED;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductMapper productMapper;
	private final OrderDetailMapper orderDetailMapper;

	@Override
	public Long saveProduct(ProductCreateDto dto) {

		checkPriceAndInventoryValidation(dto.getPrice(), dto.getInventory());

		Product product = dto.toEntity();
		productMapper.insert(product);

		return product.getId();
	}

	@Override
	@Transactional
	public void updateProduct(ProductUpdateDto dto) throws NotFoundException {

		checkPriceAndInventoryValidation(dto.getPrice(), dto.getInventory());

		Product product = productMapper.findById(dto.getId()).orElseThrow(NotFoundException::new);

		product.updateProductInfo(dto.getCategoryId(), dto.getName(), dto.getPrice(), dto.getInventory(), dto.getStatus());
		productMapper.update(product);
	}

	@Override
	@Transactional
	public void deleteProduct(Long productId) throws NotFoundException {

		productMapper.findById(productId).orElseThrow(NotFoundException::new);
		List<OrderDetail> orderDetails = orderDetailMapper.findByProductId(productId)
			.stream().filter(orderDetail -> orderDetail.getStatus() != DELIVERED).toList();

		if (!orderDetails.isEmpty()) {
			throw new BusinessRuleViolationException("주문 상품의 배송이 완료되지 않았으면 상품을 삭제할 수 없습니다.");
		}

		productMapper.updateIsDeleted(productId, TRUE);
	}

	@Override
	public ProductResponseDto getProduct(Long productId) {

		Product product = productMapper.findById(productId).orElseThrow(NotFoundException::new);

		return new ProductResponseDto().toEntity(product);
	}

	@Override
	public List<ProductResponseDto> getProducts(String keyword, Long categoryId, Long sellerId, int page, Sorter sorter) {

		int limit = 10;
		int offset = (page - 1) * limit;

		return productMapper.find(keyword, categoryId, sellerId, limit, offset, sorter.toString())
			.stream()
			.map(product -> new ProductResponseDto().toEntity(product))
			.toList();
	}

	private void checkPriceAndInventoryValidation(Long price, Long inventory) {

		if (price < 0 || inventory < 0) {
			throw new ValidationException("가격과 재고는 마이너스가 될 수 없습니다.");
		}
	}
}
