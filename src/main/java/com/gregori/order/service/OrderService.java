package com.gregori.order.service;

import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.dto.OrderResponseDto;

public interface OrderService {
	OrderResponseDto createOrder(OrderRequestDto orderRequestDto);
	OrderResponseDto findOrderById(Long orderId);
}
