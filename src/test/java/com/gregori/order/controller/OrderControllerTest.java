package com.gregori.order.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregori.item.domain.Item;
import com.gregori.item.mapper.ItemMapper;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.order.domain.Order;
import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.mapper.OrderMapper;
import com.gregori.order_item.domain.OrderItem;
import com.gregori.order_item.dto.OrderItemRequestDto;
import com.gregori.order_item.mapper.OrderItemMapper;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
	private ItemMapper itemMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderItemMapper orderItemMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

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

	@AfterAll
	void afterAll() {
		if (!orderItemIds.isEmpty()) {
			orderItemMapper.deleteByIds(orderItemIds);
		}
		if (!orderIds.isEmpty()) {
			orderMapper.deleteByIds(orderIds);
		}
		if (!items.isEmpty()) {
			itemMapper.deleteById(items.stream().map(Item::getId).toList());
		}
		if(member != null) {
			memberMapper.deleteByEmails(List.of(member.getEmail()));
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

		actions.andDo(document("order-createOrder",
			responseFields(
				fieldWithPath("result").description("요청에 대한 응답 결과"),
				fieldWithPath("httpStatus").description("요청에 대한 http 상태"),
				fieldWithPath("data").description("요청에 대한 데이터"),
				fieldWithPath("data.id").description("요청에 대한 주문 아이디"),
				fieldWithPath("data.memberId").description("요청에 대한 주문의 회원 아이디"),
				fieldWithPath("data.orderNo").description("요청에 대한 주문 번호"),
				fieldWithPath("data.paymentMethod").description("요청에 대한 주문 지불 방법"),
				fieldWithPath("data.paymentAmount").description("요청에 대한 주문 지불 금액"),
				fieldWithPath("data.deliveryCost").description("요청에 대한 주문 배송비"),
				fieldWithPath("data.status").description("요청에 대한 주문 상태"),
				fieldWithPath("data.orderItems").description("요청에 대한 주문 상품 목록"),
				fieldWithPath("data.orderItems[0].id").description("주문 상품의 아이디"),
				fieldWithPath("data.orderItems[0].orderId").description("주문 상품의 주문 번호"),
				fieldWithPath("data.orderItems[0].orderCount").description("주문 상품의 주문 개수"),
				fieldWithPath("data.orderItems[0].itemId").description("주문 상품의 상품 아이디"),
				fieldWithPath("data.orderItems[0].itemName").description("주문 상품의 상품 이름"),
				fieldWithPath("data.orderItems[0].itemPrice").description("주문 상품의 상품 가격"),
				fieldWithPath("data.orderItems[0].status").description("주문 상품의 상태"),
				fieldWithPath("errorType").description("에러가 발생한 경우 에러 타입"),
				fieldWithPath("description").description("응답에 대한 설명")
			)
		));
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

		actions.andDo(document("order-findOrderById",
			responseFields(
				fieldWithPath("result").description("요청에 대한 응답 결과"),
				fieldWithPath("httpStatus").description("요청에 대한 http 상태"),
				fieldWithPath("data").description("요청에 대한 데이터"),
				fieldWithPath("data.id").description("요청에 대한 주문 아이디"),
				fieldWithPath("data.memberId").description("요청에 대한 주문의 회원 아이디"),
				fieldWithPath("data.orderNo").description("요청에 대한 주문 번호"),
				fieldWithPath("data.paymentMethod").description("요청에 대한 주문 지불 방법"),
				fieldWithPath("data.paymentAmount").description("요청에 대한 주문 지불 금액"),
				fieldWithPath("data.deliveryCost").description("요청에 대한 주문 배송비"),
				fieldWithPath("data.status").description("요청에 대한 주문 상태"),
				fieldWithPath("data.orderItems").description("요청에 대한 주문 상품 목록"),
				fieldWithPath("data.orderItems[0].id").description("주문 상품의 아이디"),
				fieldWithPath("data.orderItems[0].orderId").description("주문 상품의 주문 번호"),
				fieldWithPath("data.orderItems[0].orderCount").description("주문 상품의 주문 개수"),
				fieldWithPath("data.orderItems[0].itemId").description("주문 상품의 상품 아이디"),
				fieldWithPath("data.orderItems[0].itemName").description("주문 상품의 상품 이름"),
				fieldWithPath("data.orderItems[0].itemPrice").description("주문 상품의 상품 가격"),
				fieldWithPath("data.orderItems[0].status").description("주문 상품의 상태"),
				fieldWithPath("errorType").description("에러가 발생한 경우 에러 타입"),
				fieldWithPath("description").description("응답에 대한 설명")
			)
		));
	}
}