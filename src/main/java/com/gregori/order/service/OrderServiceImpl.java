package com.gregori.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.common.exception.NotFoundException;
import com.gregori.product.domain.Product;
import com.gregori.product.mapper.ProductMapper;
import com.gregori.order.domain.Order;
import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.dto.OrderResponseDto;
import com.gregori.order.mapper.OrderMapper;
import com.gregori.order_detail.domain.OrderDetail;
import com.gregori.order_detail.dto.OrderDetailResponseDto;
import com.gregori.order_detail.mapper.OrderDetailMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderMapper orderMapper;
	private final ProductMapper productMapper;
	private final OrderDetailMapper orderDetailMapper;

	@Override
	@Transactional
	public OrderResponseDto saveOrder(OrderRequestDto orderRequestDto) throws NotFoundException {

		Order order = orderRequestDto.toEntity();
		orderMapper.insert(order);
		List<OrderDetailResponseDto> orderDetails = orderRequestDto.getOrderDetails()
			.stream()
			.map(orderDetailRequestDto -> {
				Product product = productMapper.findById(orderDetailRequestDto.getProductId())
					.orElseThrow(NotFoundException::new);
				OrderDetail initOrderDetail = orderDetailRequestDto.toEntity(order.getId(), product);
				orderDetailMapper.insert(initOrderDetail);

				return new OrderDetailResponseDto().toEntity(initOrderDetail);
			})
			.toList();

		return new OrderResponseDto().toEntity(order, orderDetails);
	}

	@Override
	@Transactional
	public OrderResponseDto getOrder(Long orderId) throws NotFoundException {

		Order order = orderMapper.findById(orderId)
			.orElseThrow(NotFoundException::new);
		List<OrderDetail> orderDetails = orderDetailMapper.findByOrderId(order.getId());
		if (orderDetails.isEmpty()) {
			throw new NotFoundException();
		}

		return new OrderResponseDto().toEntity(order, orderDetails.stream()
			.map(orderDetail -> new OrderDetailResponseDto().toEntity(orderDetail))
			.toList());
	}
}
