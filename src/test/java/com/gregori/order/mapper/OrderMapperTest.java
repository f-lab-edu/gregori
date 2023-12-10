package com.gregori.order.mapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.gregori.common.exception.NotFoundException;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.order.domain.Order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
class OrderMapperTest {
	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	Member member;
	List<Long> orderIds = new ArrayList<>();

	@BeforeEach
	void beforeEach() {
		member = Member.builder()
			.email("a@a.a")
			.name("일호")
			.password(passwordEncoder.encode("aa11111!"))
			.build();
		memberMapper.insert(member);
	}

	@AfterEach
	void afterEach() {
		if (!orderIds.isEmpty()) {
			orderMapper.deleteByIds(orderIds);
			orderIds.clear();
		}
		if(member != null) {
			memberMapper.deleteByIds(List.of(member.getId()));
			member = null;
		}
	}

	@Test
	@DisplayName("Orders 테이블에 새로운 주문을 삽입한다.")
	void insert() {
		// given
		Order order = Order.builder()
			.memberId(member.getId())
			.paymentMethod("카드")
			.paymentAmount(1000L)
			.deliveryCost(2500L)
			.build();

		// when
		orderMapper.insert(order);
		orderIds.add(order.getId());
		Order result = orderMapper.findById(order.getId()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result.getId(), order.getId());
		assertEquals(result.getMemberId(), order.getMemberId());
		assertEquals(result.getPaymentMethod(), order.getPaymentMethod());
	}

	@Test
	@DisplayName("Orders 테이블에서 id가 일치하는 주문을 삭제한다.")
	void deleteByIds() {
		// given
		Order order = Order.builder()
			.memberId(member.getId())
			.paymentMethod("카드")
			.paymentAmount(1000L)
			.deliveryCost(2500L)
			.build();

		orderMapper.insert(order);
		orderIds.add(order.getId());

		// when
		orderMapper.deleteByIds(List.of(order.getId()));
		Order result = orderMapper.findById(order.getId()).orElse(null);

		// then
		assertNull(result);
	}

	@Test
	@DisplayName("Orders 테이블에서 id가 일치하는 주문을 조회한다.")
	void findById() {
		// given
		Order order = Order.builder()
			.memberId(member.getId())
			.paymentMethod("카드")
			.paymentAmount(1000L)
			.deliveryCost(2500L)
			.build();

		orderMapper.insert(order);
		orderIds.add(order.getId());

		// when
		Order result = orderMapper.findById(order.getId()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result.getId(), order.getId());
		assertEquals(result.getMemberId(), order.getMemberId());
		assertEquals(result.getPaymentMethod(), order.getPaymentMethod());
		assertEquals(result.getPaymentAmount(), order.getPaymentAmount());
		assertEquals(result.getDeliveryCost(), order.getDeliveryCost());
		assertEquals(result.getCreatedAt(), order.getCreatedAt());
		assertEquals(result.getUpdatedAt(), order.getUpdatedAt());
	}
}
