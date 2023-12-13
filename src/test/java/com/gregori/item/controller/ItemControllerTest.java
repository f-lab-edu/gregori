package com.gregori.item.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.gregori.item.service.ItemService;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = ItemController.class)
class ItemControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	ItemService itemService;

	@Test
	@DisplayName("클라이언트의 요청에 따라 테이블에 저장된 상품을 조회한다.")
	void getItem() throws Exception {
		// given
		Long itemId = 1L;

		// when
		mockMvc.perform(MockMvcRequestBuilders.get("/item/" + itemId)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.httpStatus", is("OK")))
			.andExpect(jsonPath("$.result", is("SUCCESS")))
			.andDo(print());

		// then
		verify(itemService).getItem(itemId);
	}
}
