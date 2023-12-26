package com.gregori.order.dto;

import com.gregori.order.domain.OrderDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponseDto {

	private Long id;
	private Long orderId;
	private Long productId;
	private Long productSellerId;
	private String productName;
	private Long productPrice;
	private Long productCount;
	private OrderDetail.Status status;

	public OrderDetailResponseDto toEntity(OrderDetail orderDetail) {

		return OrderDetailResponseDto.builder()
			.id(orderDetail.getId())
			.orderId(orderDetail.getOrderId())
			.productId(orderDetail.getProductId())
			.productSellerId(orderDetail.getProductSellerId())
			.productName(orderDetail.getProductName())
			.productPrice(orderDetail.getProductPrice())
			.productCount(orderDetail.getProductCount())
			.status(orderDetail.getStatus())
			.build();
	}
}
