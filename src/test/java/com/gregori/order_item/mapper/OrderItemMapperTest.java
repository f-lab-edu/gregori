package com.gregori.order_item.mapper;

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

import com.gregori.item.domain.Item;
import com.gregori.item.mapper.ItemMapper;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.order.domain.Order;
import com.gregori.order.mapper.OrderMapper;
import com.gregori.order_item.domain.OrderItem;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderItemMapperTest {
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

	Long orderId = 0L;
	List<Item> items = new ArrayList<>();
	List<Long> orderItemIds = new ArrayList<>();

	@BeforeAll
	void beforeAll() {
		Member member = Member.builder()
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

		Order order = Order.builder()
			.memberId(member.getId())
			.paymentMethod("카드")
			.paymentAmount(1000L)
			.deliveryCost(2500L)
			.build();
		orderMapper.insert(order);
		orderId = order.getId();
	}

	@AfterEach
	void afterEach() {
		orderItemMapper.deleteByIds(orderItemIds);
	}

	@AfterAll
	void afterAll() {
		orderItemMapper.deleteByIds(orderItemIds);
		orderMapper.deleteByIds(List.of(orderId));
		itemMapper.deleteById(items.stream().map(Item::getId).toList());
		memberMapper.deleteByEmails(List.of("a@a.a"));
	}

	@Test
	@DisplayName("OrderItems 테이블에 새로운 주문 상품을 삽입한다.")
	void insert() {
		// given
		OrderItem orderItem = OrderItem.builder()
			.orderId(orderId)
			.orderCount(2L)
			.itemId(items.get(0).getId())
			.itemName(items.get(0).getName())
			.itemPrice(items.get(0).getPrice())
			.build();

		// when
		orderItemMapper.insert(orderItem);
		orderItemIds.add(orderItem.getId());
		List<OrderItem> result = orderItemMapper.findByIds(List.of(orderItem.getId()));

		// then
		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getId(), orderItem.getId());
		assertEquals(result.get(0).getOrderCount(), orderItem.getOrderCount());
	}

	@Test
	@DisplayName("OrderItems 테이블에서 id가 일치하는 주문을 전부 삭제한다.")
	void deleteByIds() {
		// given
		OrderItem orderItem = OrderItem.builder()
			.orderId(orderId)
			.orderCount(2L)
			.itemId(items.get(0).getId())
			.itemName(items.get(0).getName())
			.itemPrice(items.get(0).getPrice())
			.build();

		orderItemMapper.insert(orderItem);
		orderItemIds.add(orderItem.getId());

		// when
		orderItemMapper.deleteByIds(List.of(orderItem.getId()));
		List<OrderItem> result = orderItemMapper.findByIds(List.of(orderItem.getId()));

		// then
		assertEquals(result.size(), 0);
	}

	@Test
	@DisplayName("OrderItems 테이블에서 orderId가 일치하는 주문 상품을 조회한다.")
	void findByOrderId() {
		// given
		OrderItem orderItem1 = OrderItem.builder()
			.orderId(orderId)
			.orderCount(2L)
			.itemId(items.get(0).getId())
			.itemName(items.get(0).getName())
			.itemPrice(items.get(0).getPrice())
			.build();
		OrderItem orderItem2 = OrderItem.builder()
			.orderId(orderId)
			.orderCount(2L)
			.itemId(items.get(1).getId())
			.itemName(items.get(1).getName())
			.itemPrice(items.get(1).getPrice())
			.build();

		orderItemMapper.insert(orderItem1);
		orderItemMapper.insert(orderItem2);
		orderItemIds.add(orderItem1.getId());
		orderItemIds.add(orderItem2.getId());

		// when
		List<OrderItem> result = orderItemMapper.findByOrderId(orderId);

		// then
		assertEquals(result.get(0).getId(), orderItem1.getId());
		assertEquals(result.get(0).getOrderCount(), orderItem1.getOrderCount());
		assertEquals(result.get(1).getId(), orderItem2.getId());
		assertEquals(result.get(1).getOrderCount(), orderItem2.getOrderCount());
	}

	@Test
	@DisplayName("OrderItems 테이블에서 id가 일치하는 주문을 전부 조회한다.")
	void findByIds() {
		// given
		OrderItem orderItem1 = OrderItem.builder()
			.orderId(orderId)
			.orderCount(2L)
			.itemId(items.get(0).getId())
			.itemName(items.get(0).getName())
			.itemPrice(items.get(0).getPrice())
			.build();
		OrderItem orderItem2 = OrderItem.builder()
			.orderId(orderId)
			.orderCount(2L)
			.itemId(items.get(1).getId())
			.itemName(items.get(1).getName())
			.itemPrice(items.get(1).getPrice())
			.build();

		orderItemMapper.insert(orderItem1);
		orderItemMapper.insert(orderItem2);
		orderItemIds.add(orderItem1.getId());
		orderItemIds.add(orderItem2.getId());

		// when
		List<OrderItem> result = orderItemMapper.findByIds(List.of(orderItem1.getId(), orderItem2.getId()));

		// then
		assertEquals(result.get(0).getId(), orderItem1.getId());
		assertEquals(result.get(0).getOrderCount(), orderItem1.getOrderCount());
		assertEquals(result.get(1).getId(), orderItem2.getId());
		assertEquals(result.get(1).getOrderCount(), orderItem2.getOrderCount());
	}
}
