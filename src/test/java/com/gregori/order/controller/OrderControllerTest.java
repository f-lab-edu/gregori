package com.gregori.order.controller;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregori.common.CustomWebMvcTest;
import com.gregori.member.domain.Member;
import com.gregori.member.domain.SessionMember;
import com.gregori.order.dto.OrderDetailStatusUpdateDto;
import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.dto.OrderDetailRequestDto;

import static com.gregori.auth.domain.Authority.GENERAL_MEMBER;
import static com.gregori.common.DeepReflectionEqMatcher.deepRefEq;
import static com.gregori.order.domain.OrderDetail.Status.PAYMENT_CANCELED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest extends CustomWebMvcTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@DisplayName("주문 생성을 요청하면 Created 응답을 반환한다.")
	void should_responseCreated_when_requestCreateOrder() throws Exception {

		// given
		List<OrderDetailRequestDto> orderDetails = List.of(new OrderDetailRequestDto(1L, 1L));
		OrderRequestDto dto = new OrderRequestDto(1L, "카드", 1000L, 12500L, orderDetails);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("member", new SessionMember(null, "a@a.a", GENERAL_MEMBER));
		Member member = new Member("name", "a@a.a", "password");

		given(memberMapper.findById(null)).willReturn(Optional.of(member));

		// when, then
		mockMvc.perform(
				MockMvcRequestBuilders.post("/order")
					.session(session)
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("주문 취소를 요청하면 NoContent 응답을 반환한다.")
	void should_responseNoContent_when_requestCancelOrder() throws Exception {

		// given
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("member", new SessionMember(null, "a@a.a", GENERAL_MEMBER));
		Member member = new Member("name", "a@a.a", "password");

		given(memberMapper.findById(null)).willReturn(Optional.of(member));

		// when, then
		mockMvc.perform(
				MockMvcRequestBuilders.patch("/order/1")
					.session(session)
					.contentType(APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("주문 상세 갱신을 요청하면 NoContent 응답을 반환한다.")
	void should_responseNoContent_when_requestUpdateOrderDetailStatus() throws Exception {

		// given
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("member", new SessionMember(null, "a@a.a", GENERAL_MEMBER));
		Member member = new Member("name", "a@a.a", "password");
		OrderDetailStatusUpdateDto dto = new OrderDetailStatusUpdateDto(1L, PAYMENT_CANCELED);

		given(memberMapper.findById(null)).willReturn(Optional.of(member));

		// when, then
		mockMvc.perform(
				MockMvcRequestBuilders.patch("/order/detail")
					.session(session)
					.contentType(APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("주문 조회를 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_requestGetOrder() throws Exception {

		// given
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("member", new SessionMember(null, "a@a.a", GENERAL_MEMBER));
		Member member = new Member("name", "a@a.a", "password");

		given(memberMapper.findById(null)).willReturn(Optional.of(member));

		// when, then
		mockMvc.perform(
				MockMvcRequestBuilders.get("/order/1")
					.session(session)
					.contentType(APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("주문 목록 조회를 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_requestGetOrders() throws Exception {

		// given
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("member", new SessionMember(null, "a@a.a", GENERAL_MEMBER));
		Member member = new Member("name", "a@a.a", "password");

		given(memberMapper.findById(null)).willReturn(Optional.of(member));

		// when, then
		mockMvc.perform(
			MockMvcRequestBuilders.get("/order?page=1")
				.session(session)
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}
