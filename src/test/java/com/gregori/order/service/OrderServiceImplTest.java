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
import com.gregori.order.dto.OrderResponseDto;
import com.gregori.order_detail.domain.OrderDetail;
import com.gregori.product.domain.Product;
import com.gregori.product.mapper.ProductMapper;
import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.mapper.OrderMapper;
import com.gregori.order_detail.dto.OrderDetailRequestDto;
import com.gregori.order_detail.mapper.OrderDetailMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
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
	@DisplayName("새로운 주문과 주문 상품을 DB에 저장하고 주문 정보를 반환한다.")
	void should_createOrder() {

		// given
		OrderRequestDto dto = new OrderRequestDto(1L, "paymentMethod", 1L, 1L, List.of());

		// when
		orderService.saveOrder(dto);

		// then
		verify(orderMapper).insert(any(Order.class));
	}

	@Test
	@DisplayName("orderId로 DB에 저장된 주문과 주문 상품을 조회해서 반환한다.")
	void should_find_when_idMatch() {

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
