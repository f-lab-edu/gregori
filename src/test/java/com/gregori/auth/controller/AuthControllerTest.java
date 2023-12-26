package com.gregori.auth.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregori.auth.dto.AuthSignInDto;
import com.gregori.auth.dto.TokenRequestDto;
import com.gregori.auth.service.AuthService;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = AuthController.class)
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	AuthService authService;

	@Test
	@DisplayName("로그인을 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_requestSignIn() throws Exception {

		// given
		AuthSignInDto dto = new AuthSignInDto("a@a.a", "aa11111!");

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.post("/auth/signin")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(authService).signIn(refEq(dto));
	}

	@Test
	@DisplayName("로그아웃을 요쳥하면 NoContent 응답을 반환한다.")
	void should_responseNoContent_when_requestSignOut() throws Exception {

		// given
		TokenRequestDto dto = new TokenRequestDto("access_token", "refresh_token");

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.post("/auth/signout")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)));

		// then
		actions.andExpect(status().isNoContent()).andDo(print());

		verify(authService).signOut(refEq(dto));
	}

	@Test
	@DisplayName("리프레시를 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_requestRefresh() throws Exception {

		// given
		TokenRequestDto dto = new TokenRequestDto("access_token", "refresh_token");

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.post("/auth/refresh")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(authService).refresh(refEq(dto));
	}
}
