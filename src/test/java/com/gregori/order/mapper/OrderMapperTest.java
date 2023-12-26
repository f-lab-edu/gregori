package com.gregori.order.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gregori.common.CustomMybatisTest;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.order.domain.Order;

import static com.gregori.order.domain.Order.Status.ORDER_COMPLETED;
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
		Optional<Order> result = orderMapper.findById(order.getId());

		// then
		assertThat(result.isPresent()).isTrue();
	}

	@Test
	@DisplayName("주문의 상태를 수정한다.")
	void should_updateStatus() {

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
		order.orderCompleted();
		orderMapper.updateStatus(order.getId(), order.getStatus());
		Optional<Order> result = orderMapper.findById(order.getId());

		// then
		assertThat(result.isPresent()).isTrue();
		assertThat(result.get().getStatus()).isEqualTo(ORDER_COMPLETED);
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
		Optional<Order> result = orderMapper.findById(order.getId());

		// then
		assertThat(result.isPresent()).isTrue();
		assertThat(result.get().getId()).isEqualTo(order.getId());
		assertThat(result.get().getMemberId()).isEqualTo(order.getMemberId());
		assertThat(result.get().getPaymentMethod()).isEqualTo(order.getPaymentMethod());
		assertThat(result.get().getPaymentAmount()).isEqualTo(order.getPaymentAmount());
		assertThat(result.get().getDeliveryCost()).isEqualTo(order.getDeliveryCost());
		assertThat(result.get().getCreatedAt()).isEqualTo(order.getCreatedAt());
		assertThat(result.get().getUpdatedAt()).isEqualTo(order.getUpdatedAt());
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
