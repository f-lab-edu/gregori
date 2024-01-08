package com.gregori.member.controller;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregori.common.CustomWebMvcTest;
import com.gregori.member.domain.Member;
import com.gregori.member.domain.SessionMember;
import com.gregori.member.dto.MemberNameUpdateDto;
import com.gregori.member.dto.MemberPasswordUpdateDto;
import com.gregori.member.dto.MemberRegisterDto;

import jakarta.servlet.http.Cookie;

import static com.gregori.auth.domain.Authority.GENERAL_MEMBER;
import static com.gregori.common.CookieGenerator.COOKIE_NAME;
import static com.gregori.common.DeepReflectionEqMatcher.deepRefEq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends CustomWebMvcTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@DisplayName("회원가입을 요청하면 Created 응답을 반환한다.")
	void should_responseCreated_when_requestRegister() throws Exception {

		// given
		MemberRegisterDto dto = new MemberRegisterDto("일호", "a@a.a", "aa11111!");

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.post("/member/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)));

		// then
		actions.andExpect(status().isCreated());

		verify(memberService).register(deepRefEq(dto));
	}

	@Test
	@DisplayName("회원 이름 수정을 요청하면 NoContent 응답을 반환한다.")
	void should_responseNoContent_when_requestUpdateMemberName() throws Exception {

		// given
		MemberNameUpdateDto dto = new MemberNameUpdateDto("이름");

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("member", new SessionMember(null, "a@a.a", GENERAL_MEMBER));
		Member member = new Member("name", "a@a.a", "password");

		given(memberMapper.findById(null)).willReturn(Optional.of(member));

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.post("/member/name")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)));

		// then
		actions.andExpect(status().isNoContent());

		verify(memberService).updateMemberName(any(), any());
	}

	@Test
	@DisplayName("회원 비밀번호 수정을 요청하면 NoContent 응답을 반환한다.")
	void should_responseNoContent_when_requestUpdateMemberPassword() throws Exception {

		// given
		MemberPasswordUpdateDto dto = new MemberPasswordUpdateDto("aa11111!", "aa11111!");

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("member", new SessionMember(null, "a@a.a", GENERAL_MEMBER));
		Member member = new Member("name", "a@a.a", "password");

		given(memberMapper.findById(null)).willReturn(Optional.of(member));

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.post("/member/password")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)));

		// then
		actions.andExpect(status().isNoContent());

		verify(memberService).updateMemberPassword(any(), deepRefEq(dto));
	}

	@Test
	@DisplayName("회원 탈퇴를 요청하면 NoContent 응답을 반환한다.")
	void should_responseNoContent_when_requestDeleteMember() throws Exception {

		// given
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("member", new SessionMember(null, "a@a.a", GENERAL_MEMBER));
		Cookie cookie = new Cookie(COOKIE_NAME, "0");
		Member member = new Member("name", "a@a.a", "password");

		given(memberMapper.findById(null)).willReturn(Optional.of(member));

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.delete("/member")
				.session(session)
				.cookie(cookie)
				.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isNoContent());

		verify(memberService).deleteMember(any());
	}

	@Test
	@DisplayName("회원 조회를 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_requestGetMember() throws Exception {

		// given
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("member", new SessionMember(null, "a@a.a", GENERAL_MEMBER));
		Member member = new Member("name", "a@a.a", "password");

		given(memberMapper.findById(null)).willReturn(Optional.of(member));

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.get("/member")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk());

		verify(memberService).getMember(any());
	}
}
