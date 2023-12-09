package com.gregori.item.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.gregori.item.domain.Item;
import com.gregori.item.mapper.ItemMapper;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ItemControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ItemMapper itemMapper;

	List<Long> itemIds = new ArrayList<>();

	@AfterEach
	void AfterEach() {
		if (!itemIds.isEmpty()) {
			itemMapper.deleteById(itemIds);
			itemIds.clear();
		}
	}

	@Test
	@DisplayName("클라이언트의 요청에 따라 테이블에 저장된 상품을 조회한다.")
	void getItem() throws Exception {
		// given
		Item item = Item.builder()
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();
		itemMapper.insert(item);
		itemIds.add(item.getId());

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
	}
}
