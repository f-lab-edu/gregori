package com.gregori.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.common.exception.NotFoundException;
import com.gregori.item.domain.Item;
import com.gregori.item.mapper.ItemMapper;
import com.gregori.order.domain.Order;
import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.dto.OrderResponseDto;
import com.gregori.order.mapper.OrderMapper;
import com.gregori.order_item.domain.OrderItem;
import com.gregori.order_item.dto.OrderItemResponseDto;
import com.gregori.order_item.mapper.OrderItemMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final OrderMapper orderMapper;
	private final ItemMapper itemMapper;
	private final OrderItemMapper orderItemMapper;

	@Override
	@Transactional
	public OrderResponseDto saveOrder(OrderRequestDto orderRequestDto) throws NotFoundException {
		Order order = orderRequestDto.toEntity();
		orderMapper.insert(order);
		List<OrderItemResponseDto> orderItems = orderRequestDto.getOrderItems()
			.stream()
			.map(orderItemRequestDto -> {
				Item item = itemMapper.findById(orderItemRequestDto.getItemId())
					.orElseThrow(NotFoundException::new);
				OrderItem initOrderItem = orderItemRequestDto.toEntity(order.getId(), item);
				orderItemMapper.insert(initOrderItem);

				return new OrderItemResponseDto().toEntity(initOrderItem);
			})
			.toList();

		return new OrderResponseDto().toEntity(order, orderItems);
	}

	@Override
	@Transactional
	public OrderResponseDto getOrder(Long orderId) throws NotFoundException {
		Order order = orderMapper.findById(orderId)
			.orElseThrow(NotFoundException::new);
		List<OrderItem> orderItems = orderItemMapper.findByOrderId(order.getId());
		if (orderItems.isEmpty()) {
			throw new NotFoundException();
		}

		return new OrderResponseDto().toEntity(order, orderItems.stream()
			.map(orderItem -> new OrderItemResponseDto().toEntity(orderItem))
			.toList());
	}
}
