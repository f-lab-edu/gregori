package com.gregori.member.controller;

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
import com.gregori.member.dto.MemberRegisterDto;
import com.gregori.member.service.MemberService;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = MemberController.class)
class MemberControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	MemberService memberService;

	@Test
	@DisplayName("클라이언트의 요청에 따라 신규 회원을 등록한다.")
	void should_register() throws Exception {

		// given
		MemberRegisterDto memberRegisterDto = new MemberRegisterDto("일호", "a@a.a", "aa11111!");

		// when
		mockMvc.perform(MockMvcRequestBuilders.post("/member/register")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(memberRegisterDto)))
			.andExpect(status().isCreated())
			.andDo(print());

		// then
		verify(memberService).register(refEq(memberRegisterDto));
	}
}
