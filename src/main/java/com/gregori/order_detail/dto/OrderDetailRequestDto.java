package com.gregori.order_detail.dto;

import com.gregori.product.domain.Product;
import com.gregori.order_detail.domain.OrderDetail;

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
			.productName(product.getName())
			.productPrice(product.getPrice())
			.productCount(productCount)
			.build();
	}
}
