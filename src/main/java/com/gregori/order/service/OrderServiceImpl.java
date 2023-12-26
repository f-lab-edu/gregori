package com.gregori.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.common.exception.NotFoundException;
import com.gregori.common.exception.ValidationException;
import com.gregori.product.domain.Product;
import com.gregori.product.mapper.ProductMapper;
import com.gregori.order.domain.Order;
import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.dto.OrderResponseDto;
import com.gregori.order.mapper.OrderMapper;
import com.gregori.order.domain.OrderDetail;
import com.gregori.order.dto.OrderDetailResponseDto;
import com.gregori.order.mapper.OrderDetailMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final ProductMapper productMapper;
	private final OrderMapper orderMapper;
	private final OrderDetailMapper orderDetailMapper;

	@Override
	@Transactional
	public Long saveOrder(OrderRequestDto dto) throws NotFoundException {

		Order order = dto.toEntity();
		orderMapper.insert(order);
		dto.getOrderDetails()
			.forEach(orderDetailDto -> {
				Product product = productMapper.findById(orderDetailDto.getProductId()).orElseThrow(NotFoundException::new);
				long newInventory = product.getInventory() - orderDetailDto.getProductCount();
				if (newInventory < 0) {
					throw new ValidationException("상품의 재고가 부족해서 주문을 진행할 수 없습니다.");
				}

				productMapper.updateInventory(product.getId(), newInventory);
				OrderDetail initOrderDetail = orderDetailDto.toEntity(order.getId(), product);
				orderDetailMapper.insert(initOrderDetail);
			});

		return order.getId();
	}

	@Override
	@Transactional
	public OrderResponseDto getOrder(Long orderId) throws NotFoundException {

		Order order = orderMapper.findById(orderId).orElseThrow(NotFoundException::new);
		List<OrderDetail> orderDetails = orderDetailMapper.findByOrderId(orderId);
		if (orderDetails.isEmpty()) {
			throw new NotFoundException();
		}

		return new OrderResponseDto().toEntity(order, orderDetails.stream()
			.map(orderDetail -> new OrderDetailResponseDto().toEntity(orderDetail))
			.toList());
	}
}
