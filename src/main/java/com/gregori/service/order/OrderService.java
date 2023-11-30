package com.gregori.service.order;

import com.gregori.dto.order.OrderRequestDto;
import com.gregori.dto.order.OrderResponseDto;

public interface OrderService {
	Long createOrder(OrderRequestDto orderRequestDto);
	OrderResponseDto findOrderById(Long orderId);
}
