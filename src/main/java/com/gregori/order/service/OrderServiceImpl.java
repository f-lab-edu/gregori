package com.gregori.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.order.domain.Order;
import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.dto.OrderResponseDto;
import com.gregori.order.mapper.OrderMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final OrderMapper orderMapper;

	@Override
	@Transactional
	public Long createOrder(OrderRequestDto orderRequestDto) {
		return orderMapper.insert(orderRequestDto.toEntity());
	}

	@Override
	@Transactional
	public OrderResponseDto findOrderById(Long orderId) {
		Order order = orderMapper.findById(orderId)
			.orElseThrow(() -> new RuntimeException("Order entity not found by id"));

		return new OrderResponseDto().toEntity(order);
	}
}