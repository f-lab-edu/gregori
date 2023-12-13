package com.gregori.auth.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregori.auth.dto.AuthSignInDto;
import com.gregori.auth.dto.TokenRequestDto;
import com.gregori.auth.service.AuthService;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = AuthController.class)
class AuthControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	AuthService authService;

	@Test
	@DisplayName("클라이언트의 요청에 따라 로그인 로직을 실행하고 토큰을 반환한다.")
	void signIn() throws Exception {
		// given
		AuthSignInDto authSignInDto = new AuthSignInDto("a@a.a", "aa11111!");

		// when
		mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(authSignInDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.httpStatus", is("OK")))
				.andExpect(jsonPath("$.result", is("SUCCESS")))
				.andDo(print());

		// then
		verify(authService).signIn(refEq(authSignInDto));
	}

	@Test
	@DisplayName("클라이언트의 요청에 따라 로그아웃 로직을 실행한다.")
	void signOut() throws Exception {
		// given
		TokenRequestDto tokenRequestDto = new TokenRequestDto("access_token", "refresh_token");

		// when
		mockMvc.perform(MockMvcRequestBuilders.post("/auth/signout")
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(tokenRequestDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.httpStatus", is("OK")))
			.andExpect(jsonPath("$.result", is("SUCCESS")))
			.andDo(print());

		// then
		verify(authService).signOut(refEq(tokenRequestDto));
	}
}
