package com.gregori.order.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.gregori.common.exception.NotFoundException;
import com.gregori.item.domain.Item;
import com.gregori.item.mapper.ItemMapper;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.order.domain.Order;
import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.dto.OrderResponseDto;
import com.gregori.order.mapper.OrderMapper;
import com.gregori.order_item.domain.OrderItem;
import com.gregori.order_item.dto.OrderItemRequestDto;
import com.gregori.order_item.mapper.OrderItemMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderServiceImplTest {
	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderItemMapper orderItemMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private OrderService orderService;

	Member member;
	List<Item> items = new ArrayList<>();
	List<Long> orderIds = new ArrayList<>();
	List<Long> orderItemIds = new ArrayList<>();

	@BeforeAll
	void beforeAll() {
		member = Member.builder()
			.email("a@a.a")
			.name("일호")
			.password(passwordEncoder.encode("aa11111!"))
			.build();
		memberMapper.insert(member);

		Item item1 = Item.builder()
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();
		Item item2 = Item.builder()
			.name("아이템2")
			.price(200L)
			.inventory(2L)
			.build();

		itemMapper.insert(item1);
		itemMapper.insert(item2);
		items.add(item1);
		items.add(item2);
	}

	@AfterEach
	void afterEach() {
		orderItemMapper.deleteByIds(orderItemIds);
	}

	@AfterAll
	void afterAll() {
		orderItemMapper.deleteByIds(orderItemIds);
		orderMapper.deleteByIds(orderIds);
		itemMapper.deleteById(items.stream().map(Item::getId).toList());
		memberMapper.deleteByEmails(List.of(member.getEmail()));
	}

	@Test
	@DisplayName("새로운 주문과 주문 상품을 DB에 저장하고 주문 정보를 반환한다.")
	void createOrder() {
		// given
		List<OrderItemRequestDto> orderItemsRequest = List.of(new OrderItemRequestDto(1L, items.get(0).getId()));
		OrderRequestDto orderRequestDto = new OrderRequestDto(member.getId(), "카드", 1000L, 12500L, orderItemsRequest);

		// when
		OrderResponseDto result = orderService.saveOrder(orderRequestDto);
		Order order = orderMapper.findById(result.getId()).orElseThrow(NotFoundException::new);
		List<OrderItem> orderItems = orderItemMapper.findByOrderId(order.getId());

		orderIds.add(order.getId());
		orderItemIds.add(orderItems.get(0).getId());

		// then
		assertEquals(result.getId(), order.getId());
		assertEquals(result.getMemberId(), order.getMemberId());
		assertEquals(result.getPaymentMethod(), order.getPaymentMethod());
		assertEquals(result.getOrderItems().size(), 1);
		assertEquals(orderItems.size(), 1);
		assertEquals(result.getOrderItems().get(0).getId(), orderItems.get(0).getId());
		assertEquals(result.getOrderItems().get(0).getOrderId(), orderItems.get(0).getOrderId());
	}

	@Test
	@DisplayName("orderId로 DB에 저장된 주문과 주문 상품을 조회해서 반환한다.")
	void findOrderById() {
		// given
		Order order = Order.builder()
			.memberId(member.getId())
			.paymentMethod("카드")
			.paymentAmount(1000L)
			.deliveryCost(2500L)
			.build();
		orderMapper.insert(order);
		orderIds.add(order.getId());

		OrderItem orderItem = OrderItem.builder()
			.orderId(order.getId())
			.orderCount(2L)
			.itemId(items.get(0).getId())
			.itemName(items.get(0).getName())
			.itemPrice(items.get(0).getPrice())
			.build();
		orderItemMapper.insert(orderItem);
		orderItemIds.add(orderItem.getId());

		// when
		OrderResponseDto result = orderService.getOrder(order.getId());

		// then
		assertEquals(result.getId(), order.getId());
		assertEquals(result.getMemberId(), order.getMemberId());
		assertEquals(result.getPaymentMethod(), order.getPaymentMethod());
		assertEquals(result.getStatus(), order.getStatus());
		assertEquals(result.getOrderItems().size(), 1);
		assertEquals(result.getOrderItems().get(0).getId(), orderItem.getId());
		assertEquals(result.getOrderItems().get(0).getOrderId(), orderItem.getOrderId());
	}
}
