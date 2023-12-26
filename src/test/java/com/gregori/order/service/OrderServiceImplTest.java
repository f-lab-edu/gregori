package com.gregori.order.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gregori.order.domain.Order;
import com.gregori.order_detail.domain.OrderDetail;
import com.gregori.product.mapper.ProductMapper;
import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.mapper.OrderMapper;
import com.gregori.order_detail.mapper.OrderDetailMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

	@Mock
	private ProductMapper productMapper;

	@Mock
	private OrderMapper orderMapper;

	@Mock
	private OrderDetailMapper orderDetailMapper;

	@InjectMocks
	private OrderServiceImpl orderService;

	@Test
	@DisplayName("주문 생성을 성공하면 id를 반환한다.")
	void should_returnId_when_saveOrderSuccess() {

		// given
		OrderRequestDto dto = new OrderRequestDto(1L, "paymentMethod", 1L, 1L, List.of());

		// when
		orderService.saveOrder(dto);

		// then
		verify(orderMapper).insert(any(Order.class));
	}

	@Test
	@DisplayName("주문 조회를 성공하면 주문을 반환한다.")
	void should_returnOrderResponseDto_when_getOrderSuccess() {

		// given
		Long orderId = 1L;
		Order order = new Order(1L, "method", 1L, 1L);
		OrderDetail orderDetail = new OrderDetail(1L, 1L, "name", 1L, 1L);

		given(orderMapper.findById(orderId)).willReturn(Optional.of(order));
		given(orderDetailMapper.findByOrderId(orderId)).willReturn(List.of(orderDetail));

		// when
		orderService.getOrder(orderId);

		// then
		verify(orderMapper).findById(orderId);
		verify(orderDetailMapper).findByOrderId(orderId);
	}
}
