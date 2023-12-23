package com.gregori.order.controller;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregori.order.service.OrderService;
import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order_detail.dto.OrderDetailRequestDto;

import static com.gregori.common.DeepReflectionEqMatcher.deepRefEq;
import static org.mockito.Mockito.verify;
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
	@DisplayName("주문 생성을 요청하면 OK 응답을 반환한다.")
	void should_responseOk_when_requestCreateOrder() throws Exception {

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
		actions.andExpect(status().isOk()).andDo(print());

		verify(orderService).saveOrder(deepRefEq(dto));
	}

	@Test
	@DisplayName("주문 조회를 요청하면 OK 응답을 반환한다.")
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
}
