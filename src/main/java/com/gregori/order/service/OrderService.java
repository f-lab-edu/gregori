package com.gregori.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.common.exception.BusinessRuleViolationException;
import com.gregori.common.exception.NotFoundException;
import com.gregori.common.exception.ValidationException;
import com.gregori.order.dto.OrderDetailStatusUpdateDto;
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

import static com.gregori.order.domain.Order.Status.ORDER_COMPLETED;
import static com.gregori.order.domain.OrderDetail.Status.DELIVERED;
import static com.gregori.order.domain.OrderDetail.Status.PAYMENT_CANCELED;
import static com.gregori.order.domain.OrderDetail.Status.PAYMENT_COMPLETED;
import static com.gregori.order.domain.OrderDetail.Status.SHIPMENT_PREPARATION;
import static com.gregori.order.domain.OrderDetail.Status.SHIPPED;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final ProductMapper productMapper;
	private final OrderMapper orderMapper;
	private final OrderDetailMapper orderDetailMapper;

	@Transactional
	public Long saveOrder(OrderRequestDto orderRequestDto) throws NotFoundException {

		Order order = orderRequestDto.toEntity();
		orderMapper.insert(order);
		orderRequestDto.getOrderDetails()
			.forEach(orderDetailRequestDto -> {

				Product product = productMapper.findById(orderDetailRequestDto.getProductId()).orElseThrow(NotFoundException::new);
				long newInventory = product.getInventory() - orderDetailRequestDto.getProductCount();
				if (newInventory < 0) {
					throw new ValidationException("상품의 재고가 부족해서 주문을 진행할 수 없습니다.");
				}

				productMapper.updateInventory(product.getId(), newInventory);
				OrderDetail initOrderDetail = orderDetailRequestDto.toEntity(order.getId(), product);
				orderDetailMapper.insert(initOrderDetail);
			});

		return order.getId();
	}

	@Transactional
	public void cancelOrder(Long orderId) throws NotFoundException {

		Order order = orderMapper.findById(orderId).orElseThrow(NotFoundException::new);
		order.orderCanceled();
		orderMapper.updateStatus(orderId, order.getStatus());

		List<OrderDetail> orderDetails = orderDetailMapper.findByOrderId(orderId);
		if (orderDetails.isEmpty()) {
			throw new NotFoundException("주문한 상품을 찾을 수 없습니다.");
		}

		orderDetails.forEach(this::cancelOrderDetail);
	}

	@Transactional
	public void updateOrderDetailStatus(OrderDetailStatusUpdateDto dto) throws NotFoundException {

		OrderDetail orderDetail = orderDetailMapper.findById(dto.getOrderDetailId()).orElseThrow(NotFoundException::new);

		if (dto.getStatus() == PAYMENT_CANCELED) {
			cancelOrderDetail(orderDetail);
		} else {
			if (dto.getStatus() == DELIVERED) {
				List<OrderDetail> orderDetails = orderDetailMapper.findByOrderId(orderDetail.getOrderId())
					.stream()
					.filter(detail -> detail.getId() != dto.getOrderDetailId() && (
						detail.getStatus() == PAYMENT_COMPLETED ||
							detail.getStatus() == SHIPMENT_PREPARATION ||
							detail.getStatus() == SHIPPED))
					.toList();

				if (orderDetails.isEmpty()) {
					orderMapper.updateStatus(orderDetail.getOrderId(), ORDER_COMPLETED);
				}
			}

			orderDetailMapper.updateStatus(dto.getOrderDetailId(), dto.getStatus());
		}
	}

	private void cancelOrderDetail(OrderDetail orderDetail) {
		if (orderDetail.getStatus() == SHIPMENT_PREPARATION ||
			orderDetail.getStatus() == SHIPPED ||
			orderDetail.getStatus() == DELIVERED) {
			throw new BusinessRuleViolationException("운송이 시작된 주문 상품은 취소할 수 없습니다.");
		}

		orderDetail.paymentCanceled();
		orderDetailMapper.updateStatus(orderDetail.getId(), orderDetail.getStatus());
	}


	@Transactional
	public OrderResponseDto getOrder(Long orderId) throws NotFoundException {

		Order order = orderMapper.findById(orderId).orElseThrow(NotFoundException::new);

		return getOrderResponseDto(order);
	}

	public List<OrderResponseDto> getOrders(Long memberId, int page) {

		int limit = 10;
		int offset = (page - 1) * limit;

		return orderMapper.findByMemberId(memberId, limit, offset)
			.stream().map(this::getOrderResponseDto)
			.toList();
	}

	private OrderResponseDto getOrderResponseDto(Order order) {
		List<OrderDetailResponseDto> orderDetailResponseDto = orderDetailMapper
			.findByOrderId(order.getId())
			.stream()
			.map(orderDetail -> new OrderDetailResponseDto().toEntity(orderDetail))
			.toList();

		if (orderDetailResponseDto.isEmpty()) {
			throw new NotFoundException("주문한 상품을 찾을 수 없습니다.");
		}

		return new OrderResponseDto().toEntity(order, orderDetailResponseDto);
	}
}
