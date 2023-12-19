package com.gregori.product.controller;

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

import com.gregori.product.domain.Product;
import com.gregori.product.mapper.ProductMapper;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.seller.domain.Seller;
import com.gregori.seller.mapper.SellerMapper;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private SellerMapper sellerMapper;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	Member member;
	Seller seller;
	List<Long> itemIds = new ArrayList<>();

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
	}

	@AfterEach
	void afterEach() {
		if (!itemIds.isEmpty()) {
			productMapper.deleteByIds(itemIds);
			itemIds.clear();
		}
		if (seller != null) {
			sellerMapper.deleteByIds(List.of(seller.getId()));
			seller = null;
		}
		if(member != null) {
			memberMapper.deleteById(member.getId());
			member = null;
		}
	}

	@Test
	@DisplayName("클라이언트의 요청에 따라 테이블에 저장된 상품을 조회한다.")
	void getItem() throws Exception {

		// given
		Product product = Product.builder()
			.sellerId(seller.getId())
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();
		productMapper.insert(product);
		itemIds.add(product.getId());

		// when
		ResultActions actions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/product/" + product.getId())
				.contentType(MediaType.APPLICATION_JSON)
		);

		// then
		actions.andExpect(status().isOk())
			.andExpect(jsonPath("$.result", is("SUCCESS")))
			.andExpect(jsonPath("$.httpStatus", is("OK")))
			.andExpect(jsonPath("$.data", is(notNullValue())))
			.andExpect(jsonPath("$.data.id", is(notNullValue())))
			.andExpect(jsonPath("$.data.sellerId", is(notNullValue())))
			.andExpect(jsonPath("$.data.name", is(notNullValue())))
			.andExpect(jsonPath("$.data.price", is(notNullValue())))
			.andExpect(jsonPath("$.data.inventory", is(notNullValue())))
			.andExpect(jsonPath("$.data.status", is(notNullValue())))
			.andExpect(jsonPath("$.description", is(notNullValue())))
			.andDo(print());
	}
}
