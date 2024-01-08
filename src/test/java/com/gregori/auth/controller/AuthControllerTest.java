package com.gregori.auth.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregori.auth.domain.Authority;
import com.gregori.auth.dto.AuthSignInDto;
import com.gregori.common.CustomWebMvcTest;
import com.gregori.member.domain.SessionMember;

import jakarta.servlet.http.Cookie;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends CustomWebMvcTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@DisplayName("로그인을 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_requestSignIn() throws Exception {

		// given
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("member", new SessionMember(1L, "a@a.a", Authority.GENERAL_MEMBER));
		AuthSignInDto dto = new AuthSignInDto("a@a.a", "aa11111!");

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.post("/auth/signin")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)));

		// then
		actions.andExpect(status().isOk());
	}

	@Test
	@DisplayName("로그아웃을 요쳥하면 NoContent 응답을 반환한다.")
	void should_responseNoContent_when_requestSignOut() throws Exception {

		// given
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("member", new SessionMember(1L, "a@a.a", Authority.GENERAL_MEMBER));
		Cookie cookie = new Cookie("JSESSIONID", "0");

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.post("/auth/signout")
				.session(session)
				.cookie(cookie)
				.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isNoContent());
	}
}
