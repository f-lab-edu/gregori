package com.gregori.mypage.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregori.member.dto.MemberUpdateDto;
import com.gregori.member.service.MemberService;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = MypageController.class)
class MypageControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	MemberService memberService;

	@Test
	@DisplayName("회원 수정을 요청하면 회원 정보를 갱신하고 성공 응답을 반환한다.")
	void should_responseSuccess_when_requestUpdateMember() throws Exception {

		// given
		MemberUpdateDto memberUpdateDto = new MemberUpdateDto(1L, "이름", "aa11111!");
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);

		given(securityContext.getAuthentication()).willReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		given(authentication.getName()).willReturn("1");

		// when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/mypage")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(memberUpdateDto)));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(memberService).updateMember(refEq(memberUpdateDto));
	}

	@Test
	@DisplayName("회원 탈퇴를 요청하면 회원 계정을 탈퇴하고 성공 응답을 반환한다.")
	void should_responseSuccess_when_requestDeleteMember() throws Exception {

		// given
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);

		given(securityContext.getAuthentication()).willReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		given(authentication.getName()).willReturn("1");

		// when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.delete("/mypage/1")
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(memberService).deleteMember(1L);
	}

	@Test
	@DisplayName("회원 조회를 요청하면 회원 정보를 조회하고 성공 응답을 반환한다.")
	void should_responseSuccess_when_requestGetMember() throws Exception {

		// given
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);

		given(securityContext.getAuthentication()).willReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		given(authentication.getName()).willReturn("1");

		// when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/mypage/1")
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(memberService).getMember(1L);
	}

	@Test
	@DisplayName("회원 id가 토큰 id와 불일치하면 AccessDeniedException이 발생한다.")
	void should_AccessDeniedException_when_invalidMemberId() throws Exception {

		// given
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);

		given(securityContext.getAuthentication()).willReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		given(authentication.getName()).willReturn("2");

		// when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/mypage/1")
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isForbidden()).andDo(print());
	}
}
