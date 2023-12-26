package com.gregori.order.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gregori.common.exception.BusinessRuleViolationException;
import com.gregori.common.exception.NotFoundException;
import com.gregori.common.exception.UnauthorizedException;
import com.gregori.common.exception.ValidationException;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.order.domain.Order;
import com.gregori.order.domain.OrderDetail;
import com.gregori.order.dto.OrderDetailRequestDto;
import com.gregori.product.domain.Product;
import com.gregori.product.mapper.ProductMapper;
import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.mapper.OrderMapper;
import com.gregori.order.mapper.OrderDetailMapper;

import static com.gregori.order.domain.Order.Status.ORDER_CANCELED;
import static com.gregori.order.domain.Order.Status.ORDER_COMPLETED;
import static com.gregori.order.domain.OrderDetail.Status.DELIVERED;
import static com.gregori.order.domain.OrderDetail.Status.PAYMENT_CANCELED;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

	@Mock
	private MemberMapper memberMapper;

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
	@DisplayName("상품의 재고가 부족하면 주문 생성을 실패한다.")
	void should_ValidationException_when_outOfStock() {

		// given
		OrderDetailRequestDto orderDetailRequestDto = new OrderDetailRequestDto(1L, 10L);
		OrderRequestDto orderRequestDto = new OrderRequestDto(1L, "paymentMethod", 1L, 1L, List.of(orderDetailRequestDto));
		Product product = new Product(1L, 1L, "name", 1L, 1L);

		given(productMapper.findById(1L)).willReturn(Optional.of(product));

		// when, then
		assertThrows(ValidationException.class, () -> orderService.saveOrder(orderRequestDto));
	}

	@Test
	@DisplayName("주문 취소를 성공한다.")
	void should_cancelOrderSuccess() {

		// given
		Long memberId = 1L;
		Long orderId = 1L;
		Member member = new Member("name", "email", "password");
		Order order = new Order(1L, "method", 1L, 1L);
		OrderDetail orderDetail = new OrderDetail(1L, 1L, 1L, "name", 1L, 1L);

		given(memberMapper.findById(memberId)).willReturn(Optional.of(member));
		given(orderMapper.findById(orderId)).willReturn(Optional.of(order));
		given(orderDetailMapper.findByOrderId(orderId)).willReturn(List.of(orderDetail));

		// when
		orderService.cancelOrder(memberId, orderId);

		// then
		verify(orderMapper).updateStatus(orderId, ORDER_CANCELED);
		verify(orderDetailMapper).updateStatus(null, PAYMENT_CANCELED);
	}

	@Test
	@DisplayName("회원 정보 조회를 실패하면 주문 취소를 실패한다.")
	void should_NotFoundException_when_findMemberFailure() {

		// given
		given(memberMapper.findById(1L)).willReturn(Optional.empty());

		// when, then
		assertThrows(NotFoundException.class, () -> orderService.cancelOrder(1L, 1L));
	}

	@Test
	@DisplayName("판매자 회원이면 주문 취소를 실패한다.")
	void should_UnauthorizedException_when_sellingMember() {

		// given
		Member member = new Member("name", "email", "password");
		member.sellingMember();

		given(memberMapper.findById(1L)).willReturn(Optional.of(member));

		// when, then
		assertThrows(UnauthorizedException.class, () -> orderService.cancelOrder(1L, 1L));
	}

	@Test
	@DisplayName("주문 상세를 찾을 수 없으면 주문 취소를 실패한다.")
	void should_NotFonudException_when_findOrderDetailFailure() {

		// given
		Long memberId = 1L;
		Long orderId = 1L;
		Member member = new Member("name", "email", "password");
		Order order = new Order(1L, "method", 1L, 1L);

		given(memberMapper.findById(memberId)).willReturn(Optional.of(member));
		given(orderMapper.findById(orderId)).willReturn(Optional.of(order));
		given(orderDetailMapper.findByOrderId(orderId)).willReturn(List.of());

		// when, then
		assertThrows(NotFoundException.class, () -> orderService.cancelOrder(memberId, orderId));
	}

	@Test
	@DisplayName("주문 상세 취소를 성공한다.")
	void should_cancelOrderDetailSuccess() {

		// given
		Long orderDetailId = 1L;
		OrderDetail orderDetail = new OrderDetail(1L, 1L, 1L, "name", 1L, 1L);

		given(orderDetailMapper.findById(orderDetailId)).willReturn(Optional.of(orderDetail));

		// when
		orderService.cancelOrderDetail(orderDetailId);

		// then
		verify(orderDetailMapper).updateStatus(null, PAYMENT_CANCELED);
	}

	@Test
	@DisplayName("주문 상품의 운송이 시작되면 주문 취소를 실패한다.")
	void should_BusinessRuleViolationException_when_preparedDelivery() {

		// given
		Long memberId = 1L;
		Long orderId = 1L;
		Long orderDetailId = 1L;
		Member member = new Member("name", "email", "password");
		Order order = new Order(1L, "method", 1L, 1L);
		OrderDetail orderDetail = new OrderDetail(1L, 1L, 1L, "name", 1L, 1L);
		orderDetail.shipmentPreparation();

		given(memberMapper.findById(memberId)).willReturn(Optional.of(member));
		given(orderMapper.findById(orderId)).willReturn(Optional.of(order));
		given(orderDetailMapper.findByOrderId(orderId)).willReturn(List.of(orderDetail));
		given(orderDetailMapper.findById(orderDetailId)).willReturn(Optional.of(orderDetail));

		// when, then
		assertThrows(BusinessRuleViolationException.class, () -> orderService.cancelOrder(memberId, orderId));
		assertThrows(BusinessRuleViolationException.class, () -> orderService.cancelOrderDetail(orderDetailId));
	}

	@Test
	@DisplayName("주문 상품의 상태를 갱신한다.")
	void should_updateOrderDetailStatus() {

		// given
		Long memberId = 1L;
		Long orderId = 1L;
		Long orderDetailId = 1L;
		OrderDetail.Status status = DELIVERED;
		Member member = new Member("name", "email", "password");
		member.sellingMember();
		OrderDetail orderDetail = new OrderDetail(1L, 1L, 1L, "name", 1L, 1L);
		orderDetail.delivered();

		given(memberMapper.findById(memberId)).willReturn(Optional.of(member));
		given(orderDetailMapper.findById(orderDetailId)).willReturn(Optional.of(orderDetail));
		given(orderDetailMapper.findByOrderId(orderId)).willReturn(List.of(orderDetail));

		// when
		orderService.updateOrderDetailStatus(memberId, orderDetailId, status);

		// then
		verify(orderMapper).updateStatus(orderId, ORDER_COMPLETED);
		verify(orderDetailMapper).updateStatus(orderDetailId, status);
	}

	@Test
	@DisplayName("일반 회원이면 주문 상품 상태 갱신을 실패한다.")
	void should_UnauthorizedException_when_generalMember() {

		// given
		Member member = new Member("name", "email", "password");

		given(memberMapper.findById(1L)).willReturn(Optional.of(member));

		// when, then
		assertThrows(UnauthorizedException.class, () -> orderService.updateOrderDetailStatus(1L, 1L, DELIVERED));
	}

	@Test
	@DisplayName("주문 조회를 성공하면 주문을 반환한다.")
	void should_returnOrderResponseDto_when_getOrderSuccess() {

		// given
		Long orderId = 1L;
		Order order = new Order(1L, "method", 1L, 1L);
		OrderDetail orderDetail = new OrderDetail(1L, 1L, 1L, "name", 1L, 1L);

		given(orderMapper.findById(orderId)).willReturn(Optional.of(order));
		given(orderDetailMapper.findByOrderId(null)).willReturn(List.of(orderDetail));

		// when
		orderService.getOrder(orderId);

		// then
		verify(orderMapper).findById(orderId);
		verify(orderDetailMapper).findByOrderId(null);
	}

	@Test
	@DisplayName("주문 조회를 실패하면 에러가 발생한다.")
	void should_NotFoundException_when_findOrderFailure() {

		// given
		Member member = new Member("name", "email", "password");

		given(memberMapper.findById(1L)).willReturn(Optional.of(member));
		given(orderMapper.findById(1L)).willReturn(Optional.empty());

		// when, then
		assertThrows(NotFoundException.class, () -> orderService.cancelOrder(1L, 1L));
		assertThrows(NotFoundException.class, () -> orderService.getOrder(1L));
	}

	@Test
	@DisplayName("주문 목록 조회를 성공하면 주문을 반환한다.")
	void should_returnOrderResponseDto_when_getOrdersSuccess() {

		// given
		Long memberId = 1L;
		int page = 1;
		Order order = new Order(1L, "method", 1L, 1L);
		OrderDetail orderDetail = new OrderDetail(1L, 1L, 1L, "name", 1L, 1L);

		given(orderMapper.findByMemberId(memberId, 10, 0)).willReturn(List.of(order));
		given(orderDetailMapper.findByOrderId(null)).willReturn(List.of(orderDetail));

		// when
		orderService.getOrders(memberId, page);

		// then
		verify(orderMapper).findByMemberId(memberId, 10, 0);
		verify(orderDetailMapper).findByOrderId(null);
	}

	@Test
	@DisplayName("주문 상품 조회를 실패하면 에러가 발생한다.")
	void should_NotFoundException_when_findOrderDetailFailure() {

		// given
		Long orderId = 1L;
		Long memberId = 1L;
		int page = 1;
		Order order = new Order(1L, "method", 1L, 1L);

		given(orderMapper.findById(orderId)).willReturn(Optional.of(order));
		given(orderMapper.findByMemberId(memberId, 10, 0)).willReturn(List.of(order));
		given(orderDetailMapper.findByOrderId(null)).willReturn(List.of());

		// when, then
		assertThrows(NotFoundException.class, () -> orderService.getOrder(orderId));
		assertThrows(NotFoundException.class, () -> orderService.getOrders(memberId, page));
	}
}
