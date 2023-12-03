package com.gregori.auth.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregori.auth.dto.TokenDto;
import com.gregori.config.jwt.TokenProvider;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.refresh_token.domain.RefreshToken;
import com.gregori.refresh_token.mapper.RefreshTokenMapper;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class AuthControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private RefreshTokenMapper refreshTokenMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private TokenProvider tokenProvider;
	@Autowired
	private AuthenticationManagerBuilder authenticationManagerBuilder;

	List<Member> members = new ArrayList<>();

	@BeforeAll
	void beforeAll() {
		members.add(Member.builder()
			.email("a@a.a")
			.name("일호")
			.password(passwordEncoder.encode("aa11111!"))
			.build());
		members.add(Member.builder()
			.email("b@b.b")
			.name("이호")
			.password(passwordEncoder.encode("bb22222@"))
			.build());

		memberMapper.insert(members.get(0));
		memberMapper.insert(members.get(1));
	}

	@AfterAll
	void AfterAll() {
		memberMapper.deleteByEmails(members.stream().map(Member::getEmail).collect(Collectors.toList()));
	}

	@Test
	@DisplayName("로그인 테스트")
	void signIn() throws Exception {
		// given
		Map<String, String> input = new HashMap<>();
		input.put("email", "a@a.a");
		input.put("password", "aa11111!");

		// when
		ResultActions actions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/auth/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input))
		);

		// then
		actions.andExpect(status().isOk())
			.andExpect(jsonPath("$.result", is("SUCCESS")))
			.andExpect(jsonPath("$.httpStatus", is("OK")))
			.andExpect(jsonPath("$.data", is(notNullValue())))
			.andExpect(jsonPath("$.data.grantType", is("bearer")))
			.andExpect(jsonPath("$.data.accessToken", is(notNullValue())))
			.andExpect(jsonPath("$.data.refreshToken", is(notNullValue())))
			.andExpect(jsonPath("$.data.accessTokenExpiresIn", is(notNullValue())))
			.andExpect(jsonPath("$.description", is(notNullValue())))
			.andDo(print());

		actions.andDo(document("auth-signin",
			requestFields(
				fieldWithPath("email").description("회원 이메일"),
				fieldWithPath("password").description("회원 비밀번호")
			), responseFields(
				fieldWithPath("result").description("요청에 대한 응답 결과"),
				fieldWithPath("httpStatus").description("요청에 대한 http 상태"),
				fieldWithPath("data").description("요청에 대한 데이터"),
				fieldWithPath("data.grantType").description("인가 유형"),
				fieldWithPath("data.accessToken").description("발급된 액세스 토큰"),
				fieldWithPath("data.refreshToken").description("발급된 리프레시 토큰"),
				fieldWithPath("data.accessTokenExpiresIn").description("액세스 토큰 만료 시간"),
				fieldWithPath("errorType").description("에러가 발생한 경우 에러 타입"),
				fieldWithPath("description").description("응답에 대한 설명")
			)
		));
	}

	@Test
	@DisplayName("로그아웃 테스트")
	void signOut() throws Exception {
		// given
		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken("a@a.a", "aa11111!");
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		TokenDto tokenDto = tokenProvider.generateToken(authentication);
		refreshTokenMapper.insert(RefreshToken.builder()
			.refreshTokenKey("1")
			.refreshTokenValue(tokenDto.getRefreshToken())
			.build());

		Map<String, String> input = new HashMap<>();
		input.put("accessToken", tokenDto.getAccessToken());
		input.put("refreshToken", tokenDto.getRefreshToken());

		// when
		ResultActions actions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/auth/signout")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input))
		);

		// then
		actions.andExpect(status().isOk())
			.andExpect(jsonPath("$.result", is("SUCCESS")))
			.andExpect(jsonPath("$.httpStatus", is("OK")))
			.andExpect(jsonPath("$.data", is(notNullValue())))
			.andExpect(jsonPath("$.description", is(notNullValue())))
			.andDo(print());

		actions.andDo(document("auth-signin",
			requestFields(
				fieldWithPath("accessToken").description("액세스 토큰"),
				fieldWithPath("refreshToken").description("리프레시 토큰")
			), responseFields(
				fieldWithPath("result").description("요청에 대한 응답 결과"),
				fieldWithPath("httpStatus").description("요청에 대한 http 상태"),
				fieldWithPath("data").description("요청에 대한 데이터"),
				fieldWithPath("errorType").description("에러가 발생한 경우 에러 타입"),
				fieldWithPath("description").description("응답에 대한 설명")
			)
		));
	}
}
