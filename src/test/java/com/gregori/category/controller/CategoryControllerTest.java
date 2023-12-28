package com.gregori.category.controller;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregori.category.dto.CategoryRequestDto;
import com.gregori.common.CustomWebMvcTest;
import com.gregori.member.domain.Member;
import com.gregori.member.domain.SessionMember;

import static com.gregori.auth.domain.Authority.ADMIN_MEMBER;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest extends CustomWebMvcTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@DisplayName("카테고리 생성을 요청하면 Created 응답을 반환한다.")
	void should_responseCreated_when_requestSaveCategory() throws Exception {

		// given
		CategoryRequestDto dto = new CategoryRequestDto("name");

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("member", new SessionMember(null, "a@a.a", ADMIN_MEMBER));
		Member member = new Member("name", "a@a.a", "password");
		member.adminMember();

		given(memberMapper.findById(null)).willReturn(Optional.of(member));

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.post("/category")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)));

		// then
		actions.andExpect(status().isCreated());

		verify(categoryService).saveCategory(dto.getName());
	}

	@Test
	@DisplayName("카테고리 이름 수정을 요청하면 NoContent 응답을 반환한다.")
	void should_responseNoContent_when_requestUpdateCategoryName() throws Exception {

		// given
		Long categoryId = 1L;
		CategoryRequestDto dto = new CategoryRequestDto("name");

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("member", new SessionMember(null, "a@a.a", ADMIN_MEMBER));
		Member member = new Member("name", "a@a.a", "password");
		member.adminMember();

		given(memberMapper.findById(null)).willReturn(Optional.of(member));

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.post("/category/" + categoryId)
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)));

		// then
		actions.andExpect(status().isNoContent());

		verify(categoryService).updateCategoryName(categoryId, dto.getName());
	}

	@Test
	@DisplayName("카테고리 삭제를 요청하면 NoContent 응답을 반환한다.")
	void should_responseNoContent_when_requestDeleteCategory() throws Exception {

		// given
		Long categoryId = 1L;

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("member", new SessionMember(null, "a@a.a", ADMIN_MEMBER));
		Member member = new Member("name", "a@a.a", "password");
		member.adminMember();

		given(memberMapper.findById(null)).willReturn(Optional.of(member));

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.delete("/category/" + categoryId)
				.session(session)
				.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isNoContent());

		verify(categoryService).deleteCategory(categoryId);
	}

	@Test
	@DisplayName("카테고리 조회를 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_requestGetCategory() throws Exception {

		// given
		Long categoryId = 1L;

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.get("/category/" + categoryId)
				.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk());

		verify(categoryService).getCategory(categoryId);
	}

	@Test
	@DisplayName("카테고리 전체 조회를 요청하면 카테고리 정보를 전부 조회하고 Ok 응답을 반환한다.")
	void should_responseOk_when_requestGetCategories() throws Exception {

		// given, when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.get("/category?page=1")
				.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk());

		verify(categoryService).getCategories(1);
	}
}
