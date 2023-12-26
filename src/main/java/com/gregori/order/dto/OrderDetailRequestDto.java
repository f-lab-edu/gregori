package com.gregori.order.dto;

import com.gregori.product.domain.Product;
import com.gregori.order.domain.OrderDetail;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderDetailRequestDto {

	@NotNull
	private Long productId;

	@NotNull
	private Long productCount;

	public OrderDetail toEntity(Long orderId, Product product) {

		return OrderDetail.builder()
			.orderId(orderId)
			.productId(productId)
			.productSellerId(product.getSellerId())
			.productName(product.getName())
			.productPrice(product.getPrice())
			.productCount(productCount)
			.build();
	}
}
