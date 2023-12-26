package com.gregori.order.controller;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregori.order.service.OrderService;
import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.dto.OrderDetailRequestDto;

import static com.gregori.common.DeepReflectionEqMatcher.deepRefEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = OrderController.class)
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	OrderService orderService;

	@Test
	@DisplayName("주문 생성을 요청하면 Created 응답을 반환한다.")
	void should_responseCreated_when_requestCreateOrder() throws Exception {

		// given
		List<OrderDetailRequestDto> orderDetails = List.of(new OrderDetailRequestDto(1L, 1L));
		OrderRequestDto dto = new OrderRequestDto(1L, "카드", 1000L, 12500L, orderDetails);

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.post("/order")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
		);

		// then
		actions.andExpect(status().isCreated()).andDo(print());

		verify(orderService).saveOrder(deepRefEq(dto));
	}

	@Test
	@DisplayName("주문 취소를 요청하면 NoContent 응답을 반환한다.")
	void should_responseNoContent_when_requestCancelOrder() throws Exception {

		// given
		Long memberId = 1L;
		Long orderId = 1L;

		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);

		given(securityContext.getAuthentication()).willReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		given(authentication.getName()).willReturn(memberId.toString());

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.patch("/order/" + orderId)
				.with(csrf())
				.contentType(APPLICATION_JSON));

		// then
		actions.andExpect(status().isNoContent()).andDo(print());

		verify(orderService).cancelOrder(memberId, orderId);
	}

	@Test
	@DisplayName("주문 상세 취소를 요청하면 NoContent 응답을 반환한다.")
	void should_responseNoContent_when_requestCancelOrderDetail() throws Exception {

		// given
		Long memberId = 1L;
		Long orderrDetailId = 1L;

		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);

		given(securityContext.getAuthentication()).willReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		given(authentication.getName()).willReturn(memberId.toString());

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.patch("/order/detail/" + orderrDetailId)
				.with(csrf())
				.contentType(APPLICATION_JSON));

		// then
		actions.andExpect(status().isNoContent()).andDo(print());

		verify(orderService).cancelOrderDetail(memberId, orderrDetailId);
	}

	@Test
	@DisplayName("주문 조회를 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_requestGetOrder() throws Exception {

		// given
		long orderId = 1L;

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.get("/order/" + orderId)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(orderService).getOrder(orderId);
	}

	@Test
	@DisplayName("주문 목록 조회를 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_requestGetOrders() throws Exception {

		// given
		Long memberId = 1L;
		int page = 1;

		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);

		given(securityContext.getAuthentication()).willReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		given(authentication.getName()).willReturn(memberId.toString());

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.get("/order?page=" + page)
				.with(csrf())
				.contentType(APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(orderService).getOrders(memberId, page);
	}
}
