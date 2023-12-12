package com.gregori.order.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregori.common.response.CustomResponse;
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
import com.gregori.seller.domain.Seller;
import com.gregori.seller.mapper.SellerMapper;

import lombok.extern.slf4j.Slf4j;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class OrderControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private SellerMapper sellerMapper;

	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderItemMapper orderItemMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	Member member;
	Seller seller;
	List<Item> items = new ArrayList<>();
	List<Long> orderIds = new ArrayList<>();
	List<Long> orderItemIds = new ArrayList<>();

	@BeforeEach
	void beforeEach() {
		member = Member.builder()
			.email("a@a.a")
			.name("일호")
			.password(passwordEncoder.encode("aa11111!"))
			.build();
		memberMapper.insert(member);

		seller = Seller.builder()
			.memberId(member.getId())
			.businessNumber("111-11-11111")
			.businessName("일호 상점")
			.build();
		sellerMapper.insert(seller);

		Item item1 = Item.builder()
			.sellerId(seller.getId())
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();
		Item item2 = Item.builder()
			.sellerId(seller.getId())
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
		if (!orderItemIds.isEmpty()) {
			orderItemMapper.deleteByIds(orderItemIds);
			orderItemIds.clear();
		}
		if (!orderIds.isEmpty()) {
			orderMapper.deleteByIds(orderIds);
			orderIds.clear();
		}
		if (!items.isEmpty()) {
			itemMapper.deleteByIds(items.stream().map(Item::getId).toList());
			items.clear();
		}
		if (seller != null) {
			sellerMapper.deleteByIds(List.of(seller.getId()));
			seller = null;
		}
		if(member != null) {
			memberMapper.deleteByEmails(List.of(member.getEmail()));
			member = null;
		}
	}

	@Test
	@DisplayName("클라이언트의 요청에 따라 주문을 새로 생성한다.")
	void createOrder() throws Exception {
		// given
		List<OrderItemRequestDto> orderItemsRequest = List.of(new OrderItemRequestDto(1L, items.get(0).getId()));
		OrderRequestDto input = new OrderRequestDto(member.getId(), "카드", 1000L, 12500L, orderItemsRequest);

		// when
		ResultActions actions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/order")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input))
		);

		CustomResponse<OrderResponseDto> result = objectMapper.readValue(
			actions.andReturn().getResponse().getContentAsString(),
			new TypeReference<>(){});
		orderIds.add(result.getData().getId());
		orderItemIds.add(result.getData().getOrderItems().get(0).getId());

		// then
		actions.andExpect(status().isOk())
			.andExpect(jsonPath("$.result", is("SUCCESS")))
			.andExpect(jsonPath("$.httpStatus", is("OK")))
			.andExpect(jsonPath("$.data", is(notNullValue())))
			.andExpect(jsonPath("$.data.id", is(notNullValue())))
			.andExpect(jsonPath("$.data.memberId", is(notNullValue())))
			.andExpect(jsonPath("$.data.orderNo", is(notNullValue())))
			.andExpect(jsonPath("$.data.paymentMethod", is(notNullValue())))
			.andExpect(jsonPath("$.data.paymentAmount", is(notNullValue())))
			.andExpect(jsonPath("$.data.deliveryCost", is(notNullValue())))
			.andExpect(jsonPath("$.data.status", is(notNullValue())))
			.andExpect(jsonPath("$.data.orderItems", is(notNullValue())))
			.andExpect(jsonPath("$.description", is(notNullValue())))
			.andDo(print());
	}

	@Test
	@DisplayName("클라이언트의 요청에 따라 주문을 조회한다.")
	void getOrder() throws Exception {
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
		ResultActions actions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/order/" + order.getId())
				.contentType(MediaType.APPLICATION_JSON)
		);

		// then
		actions.andExpect(status().isOk())
			.andExpect(jsonPath("$.result", is("SUCCESS")))
			.andExpect(jsonPath("$.httpStatus", is("OK")))
			.andExpect(jsonPath("$.data", is(notNullValue())))
			.andExpect(jsonPath("$.data.id", is(notNullValue())))
			.andExpect(jsonPath("$.data.memberId", is(notNullValue())))
			.andExpect(jsonPath("$.data.orderNo", is(notNullValue())))
			.andExpect(jsonPath("$.data.paymentMethod", is(notNullValue())))
			.andExpect(jsonPath("$.data.paymentAmount", is(notNullValue())))
			.andExpect(jsonPath("$.data.deliveryCost", is(notNullValue())))
			.andExpect(jsonPath("$.data.status", is(notNullValue())))
			.andExpect(jsonPath("$.data.orderItems", is(notNullValue())))
			.andExpect(jsonPath("$.description", is(notNullValue())))
			.andDo(print());
	}
}
