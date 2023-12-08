package com.gregori.item.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregori.item.domain.Item;
import com.gregori.item.mapper.ItemMapper;
import com.gregori.member.mapper.MemberMapper;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ItemControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ItemMapper itemMapper;

	List<Long> itemIds = new ArrayList<>();

	@BeforeAll
	void beforeAll() {
		Item item = Item.builder()
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();
		itemMapper.insert(item);
		itemIds.add(item.getId());
	}

	@AfterAll
	void AfterAll() {
		itemMapper.deleteById(itemIds);
	}

	@Test
	@DisplayName("클라이언트의 요청에 따라 테이블에 저장된 상품을 조회한다.")
	void getItem() throws Exception {
		// when
		ResultActions actions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/item/1")
				.contentType(MediaType.APPLICATION_JSON)
		);

		// then
		actions.andExpect(status().isOk())
			.andExpect(jsonPath("$.result", is("SUCCESS")))
			.andExpect(jsonPath("$.httpStatus", is("OK")))
			.andExpect(jsonPath("$.data", is(notNullValue())))
			.andExpect(jsonPath("$.data.id", is(1)))
			.andExpect(jsonPath("$.data.name", is(notNullValue())))
			.andExpect(jsonPath("$.data.price", is(notNullValue())))
			.andExpect(jsonPath("$.data.inventory", is(notNullValue())))
			.andExpect(jsonPath("$.data.status", is(notNullValue())))
			.andExpect(jsonPath("$.description", is(notNullValue())))
			.andDo(print());

		actions.andDo(document("items-findItemById",
			responseFields(
				fieldWithPath("result").description("요청에 대한 응답 결과"),
				fieldWithPath("httpStatus").description("요청에 대한 http 상태"),
				fieldWithPath("data").description("요청에 대한 데이터"),
				fieldWithPath("data.id").description("요청에 대한 아이템 아이디"),
				fieldWithPath("data.name").description("요청에 대한 아이템 이름"),
				fieldWithPath("data.price").description("요청에 대한 아이템 가격"),
				fieldWithPath("data.inventory").description("요청에 대한 아이템 재고"),
				fieldWithPath("data.status").description("요청에 대한 아이템 상태"),
				fieldWithPath("errorType").description("에러가 발생한 경우 에러 타입"),
				fieldWithPath("description").description("응답에 대한 설명")
			)
		));
	}
}
