package com.gregori.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.common.exception.BusinessRuleViolationException;
import com.gregori.common.exception.NotFoundException;
import com.gregori.common.exception.UnauthorizedException;
import com.gregori.common.exception.ValidationException;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
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

import static com.gregori.auth.domain.Authority.GENERAL_MEMBER;
import static com.gregori.auth.domain.Authority.SELLING_MEMBER;
import static com.gregori.order.domain.Order.Status.ORDER_COMPLETED;
import static com.gregori.order.domain.OrderDetail.Status.DELIVERED;
import static com.gregori.order.domain.OrderDetail.Status.PAYMENT_COMPLETED;
import static com.gregori.order.domain.OrderDetail.Status.SHIPMENT_PREPARATION;
import static com.gregori.order.domain.OrderDetail.Status.SHIPPED;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final MemberMapper memberMapper;
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
	public void cancelOrder(Long memberId, Long orderId) throws NotFoundException {

		Member member = memberMapper.findById(memberId).orElseThrow(NotFoundException::new);
		if (member.getAuthority() == SELLING_MEMBER) {
			throw new UnauthorizedException("판매자 회원은 주문 전체를 취소할 수 없습니다.");
		}
		Order order = orderMapper.findById(orderId).orElseThrow(NotFoundException::new);
		order.orderCanceled();
		orderMapper.updateStatus(orderId, order.getStatus());

		List<OrderDetail> orderDetails = orderDetailMapper.findByOrderId(orderId);
		if (orderDetails.isEmpty()) {
			throw new NotFoundException("주문한 상품을 찾을 수 없습니다.");
		}

		orderDetails.forEach(this::cancelOrderDetail);
	}

	public void cancelOrderDetail(Long orderDetailId) throws NotFoundException {

		OrderDetail orderDetail = orderDetailMapper.findById(orderDetailId).orElseThrow(NotFoundException::new);
		cancelOrderDetail(orderDetail);
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
	public void updateOrderDetailStatus(Long memberId, Long orderDetailId, OrderDetail.Status status) throws NotFoundException {

		Member member = memberMapper.findById(memberId).orElseThrow(NotFoundException::new);
		if (member.getAuthority() == GENERAL_MEMBER) {
			throw new UnauthorizedException("일반 회원은 주문 상품 상태를 변경할 수 없습니다.");
		}

		OrderDetail orderDetail = orderDetailMapper.findById(orderDetailId).orElseThrow(NotFoundException::new);

		if (status == DELIVERED) {
			List<OrderDetail> orderDetails = orderDetailMapper.findByOrderId(orderDetail.getOrderId())
				.stream()
				.filter(detail -> detail.getId() != orderDetailId && (
					detail.getStatus() == PAYMENT_COMPLETED ||
					detail.getStatus() == SHIPMENT_PREPARATION ||
					detail.getStatus() == SHIPPED))
				.toList();

			if (orderDetails.isEmpty()) {
				orderMapper.updateStatus(orderDetail.getOrderId(), ORDER_COMPLETED);
			}
		}

		orderDetailMapper.updateStatus(orderDetailId, status);
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
