package com.gregori.category.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.gregori.category.service.CategoryService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = CategoryController.class)
class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	CategoryService categoryService;

	@Test
	@DisplayName("카테고리 생성을 요청하면 Created 응답을 반환한다.")
	void should_responseCreated_when_requestSaveCategory() throws Exception {

		// given
		String categoryName = "name";

		// when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/category")
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(categoryName));

		// then
		actions.andExpect(status().isCreated()).andDo(print());

		verify(categoryService).saveCategory(categoryName);
	}

	@Test
	@DisplayName("카테고리 이름 수정을 요청하면 NoContent 응답을 반환한다.")
	void should_responseOk_when_requestUpdateCategoryName() throws Exception {

		// given
		Long categoryId = 1L;
		String categoryName = "name";

		// when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/category/" + categoryId)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(categoryName));

		// then
		actions.andExpect(status().isNoContent()).andDo(print());

		verify(categoryService).updateCategoryName(categoryId, categoryName);
	}

	@Test
	@DisplayName("카테고리 삭제를 요청하면 NoContent 응답을 반환한다.")
	void should_responseOk_when_requestDeleteCategory() throws Exception {

		// given
		Long categoryId = 1L;

		// when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.delete("/category/" + categoryId)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isNoContent()).andDo(print());

		verify(categoryService).deleteCategory(categoryId);
	}

	@Test
	@DisplayName("카테고리 조회를 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_requestGetCategory() throws Exception {

		// given
		Long categoryId = 1L;

		// when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/category/" + categoryId)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(categoryService).getCategory(categoryId);
	}

	@Test
	@DisplayName("카테고리 전체 조회를 요청하면 카테고리 정보를 전부 조회하고 Ok 응답을 반환한다.")
	void should_responseOk_when_requestGetCategories() throws Exception {

		// given, when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/category?page=1")
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(categoryService).getCategories(1);
	}
}
