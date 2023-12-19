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
import com.gregori.product.domain.Product;
import com.gregori.product.mapper.ProductMapper;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.order.domain.Order;
import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.dto.OrderResponseDto;
import com.gregori.order.mapper.OrderMapper;
import com.gregori.order_detail.domain.OrderDetail;
import com.gregori.order_detail.dto.OrderDetailRequestDto;
import com.gregori.order_detail.mapper.OrderDetailMapper;
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
	private ProductMapper productMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderDetailMapper orderDetailMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	Member member;
	Seller seller;
	List<Product> products = new ArrayList<>();
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

		Product product1 = Product.builder()
			.sellerId(seller.getId())
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();
		Product product2 = Product.builder()
			.sellerId(seller.getId())
			.name("아이템2")
			.price(200L)
			.inventory(2L)
			.build();

		productMapper.insert(product1);
		productMapper.insert(product2);
		products.add(product1);
		products.add(product2);
	}

	@AfterEach
	void afterEach() {
		if (!orderItemIds.isEmpty()) {
			orderDetailMapper.deleteByIds(orderItemIds);
			orderItemIds.clear();
		}
		if (!orderIds.isEmpty()) {
			orderMapper.deleteByIds(orderIds);
			orderIds.clear();
		}
		if (!products.isEmpty()) {
			productMapper.deleteByIds(products.stream().map(Product::getId).toList());
			products.clear();
		}
		if (seller != null) {
			sellerMapper.deleteByIds(List.of(seller.getId()));
			seller = null;
		}
		if(member != null) {
			memberMapper.deleteByIds(List.of(member.getId()));
			member = null;
		}
	}

	@Test
	@DisplayName("클라이언트의 요청에 따라 주문을 새로 생성한다.")
	void createOrder() throws Exception {
		// given
		List<OrderDetailRequestDto> orderItemsRequest = List.of(new OrderDetailRequestDto(1L, products.get(0).getId()));
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
		orderItemIds.add(result.getData().getOrderDetails().get(0).getId());

		// then
		actions.andExpect(status().isOk())
			.andExpect(jsonPath("$.result", is("SUCCESS")))
			.andExpect(jsonPath("$.httpStatus", is("OK")))
			.andExpect(jsonPath("$.data", is(notNullValue())))
			.andExpect(jsonPath("$.data.id", is(notNullValue())))
			.andExpect(jsonPath("$.data.memberId", is(notNullValue())))
			.andExpect(jsonPath("$.data.orderNumber", is(notNullValue())))
			.andExpect(jsonPath("$.data.paymentMethod", is(notNullValue())))
			.andExpect(jsonPath("$.data.paymentAmount", is(notNullValue())))
			.andExpect(jsonPath("$.data.deliveryCost", is(notNullValue())))
			.andExpect(jsonPath("$.data.status", is(notNullValue())))
			.andExpect(jsonPath("$.data.orderDetails", is(notNullValue())))
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

		OrderDetail orderDetail = OrderDetail.builder()
			.orderId(order.getId())
			.productId(products.get(0).getId())
			.productName(products.get(0).getName())
			.productPrice(products.get(0).getPrice())
			.productCount(2L)
			.build();
		orderDetailMapper.insert(orderDetail);
		orderItemIds.add(orderDetail.getId());

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
			.andExpect(jsonPath("$.data.orderNumber", is(notNullValue())))
			.andExpect(jsonPath("$.data.paymentMethod", is(notNullValue())))
			.andExpect(jsonPath("$.data.paymentAmount", is(notNullValue())))
			.andExpect(jsonPath("$.data.deliveryCost", is(notNullValue())))
			.andExpect(jsonPath("$.data.status", is(notNullValue())))
			.andExpect(jsonPath("$.data.orderDetails", is(notNullValue())))
			.andExpect(jsonPath("$.description", is(notNullValue())))
			.andDo(print());
	}
}
