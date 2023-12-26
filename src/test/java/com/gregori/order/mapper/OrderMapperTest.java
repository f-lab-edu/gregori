package com.gregori.order.mapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gregori.common.CustomMybatisTest;
import com.gregori.common.exception.NotFoundException;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.order.domain.Order;

import static org.assertj.core.api.Assertions.assertThat;

@CustomMybatisTest
class OrderMapperTest {

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private OrderMapper orderMapper;

	Member member;
	List<Long> orderIds = new ArrayList<>();

	@BeforeEach
	void beforeEach() {
		member = Member.builder()
			.email("a@a.a")
			.name("일호")
			.password("aa11111!")
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
	@DisplayName("새로운 주문을 추가한다.")
	void should_insert() {

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
		assertThat(result.getId()).isEqualTo(order.getId());
		assertThat(result.getMemberId()).isEqualTo(order.getMemberId());
		assertThat(result.getPaymentMethod()).isEqualTo(order.getPaymentMethod());
	}

	@Test
	@DisplayName("id 목록으로 주문을 삭제한다.")
	void should_deleteByIds() {

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
		assertThat(result).isNull();
	}

	@Test
	@DisplayName("id로 주문을 조회한다.")
	void should_findById() {

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
		assertThat(result.getId()).isEqualTo(order.getId());
		assertThat(result.getMemberId()).isEqualTo(order.getMemberId());
		assertThat(result.getPaymentMethod()).isEqualTo(order.getPaymentMethod());
		assertThat(result.getPaymentAmount()).isEqualTo(order.getPaymentAmount());
		assertThat(result.getDeliveryCost()).isEqualTo(order.getDeliveryCost());
		assertThat(result.getCreatedAt()).isEqualTo(order.getCreatedAt());
		assertThat(result.getUpdatedAt()).isEqualTo(order.getUpdatedAt());
	}

	@Test
	@DisplayName("memberId로 주문을 조회한다.")
	void should_findByMemberId() {

		// given
		Order order1 = Order.builder()
			.memberId(member.getId())
			.paymentMethod("카드")
			.paymentAmount(1000L)
			.deliveryCost(2500L)
			.build();

		Order order2 = Order.builder()
			.memberId(member.getId())
			.paymentMethod("카드")
			.paymentAmount(1000L)
			.deliveryCost(2500L)
			.build();

		orderMapper.insert(order1);
		orderMapper.insert(order2);

		orderIds.add(order1.getId());
		orderIds.add(order2.getId());

		// when
		List<Order> result = orderMapper.findByMemberId(member.getId(), null, null);

		// then
		assertThat(result.size()).isEqualTo(2);
	}
}
