package com.gregori.order.service;

import com.gregori.common.exception.NotFoundException;
import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.dto.OrderResponseDto;

public interface OrderService {
	OrderResponseDto saveOrder(OrderRequestDto orderRequestDto) throws NotFoundException;
	OrderResponseDto getOrder(Long orderId) throws NotFoundException;
}
