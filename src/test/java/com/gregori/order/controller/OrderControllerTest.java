package com.gregori.order.controller;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregori.common.DeepReflectionEqMatcher;
import com.gregori.order.domain.Order;
import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.dto.OrderResponseDto;
import com.gregori.order.service.OrderService;
import com.gregori.order_item.domain.OrderItem;
import com.gregori.order_item.dto.OrderItemRequestDto;
import com.gregori.order_item.dto.OrderItemResponseDto;

import static com.gregori.common.DeepReflectionEqMatcher.deepRefEq;
import static org.hamcrest.Matchers.is;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = OrderController.class)
class OrderControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean(answer = RETURNS_DEEP_STUBS)
	OrderService orderService;

	@Test
	@DisplayName("클라이언트의 요청에 따라 주문을 새로 생성한다.")
	void createOrder() throws Exception {
		// given
		List<OrderItemRequestDto> orderItemsRequest = List.of(new OrderItemRequestDto(1L, 1L));
		OrderRequestDto orderRequestDto = new OrderRequestDto(1L, "카드", 1000L, 12500L, orderItemsRequest);

		// when
		mockMvc.perform(MockMvcRequestBuilders.post("/order")
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(orderRequestDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.httpStatus", is("OK")))
			.andExpect(jsonPath("$.result", is("SUCCESS")))
			.andDo(print());

		// then
		verify(orderService, times(1)).saveOrder(deepRefEq(orderRequestDto));
	}

	@Test
	@DisplayName("클라이언트의 요청에 따라 주문을 조회한다.")
	void getOrder() throws Exception {
		// given
		Long orderId = 1L;

		// when
		mockMvc.perform(MockMvcRequestBuilders.get("/order/" + orderId)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.httpStatus", is("OK")))
			.andExpect(jsonPath("$.result", is("SUCCESS")))
			.andDo(print());

		// then
		verify(orderService).getOrder(orderId);
	}
}
