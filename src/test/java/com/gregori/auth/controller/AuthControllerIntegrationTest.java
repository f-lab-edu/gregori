package com.gregori.auth.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import com.gregori.common.exception.NotFoundException;
import com.gregori.config.jwt.TokenProvider;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.refresh_token.domain.RefreshToken;
import com.gregori.refresh_token.mapper.RefreshTokenMapper;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
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

	RefreshToken refreshToken;
	List<Member> members = new ArrayList<>();

	@BeforeEach
	void beforeEach() {
		Member member1 = Member.builder()
			.email("a@a.a")
			.name("일호")
			.password(passwordEncoder.encode("aa11111!"))
			.build();
		Member member2 = Member.builder()
			.email("b@b.b")
			.name("이호")
			.password(passwordEncoder.encode("bb22222@"))
			.build();

		memberMapper.insert(member1);
		memberMapper.insert(member2);

		members.add(member1);
		members.add(member2);
	}

	@AfterEach
	void afterEach() {
		if (refreshToken != null) {
			refreshTokenMapper.deleteById(refreshToken.getId());
			refreshToken = null;
		}

		if (!members.isEmpty()) {
			memberMapper.deleteByEmails(members.stream().map(Member::getEmail).toList());
			members.clear();
		}
	}

	@Test
	@DisplayName("클라이언트의 요청에 따라 로그인 로직을 실행하고 토큰을 반환한다.")
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

		refreshToken = refreshTokenMapper.findByRefreshTokenKey(members.get(0).getId().toString())
			.orElseThrow(NotFoundException::new);

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
	}

	@Test
	@DisplayName("클라이언트의 요청에 따라 로그아웃 로직을 실행한다.")
	void signOut() throws Exception {
		// given
		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken("a@a.a", "aa11111!");
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		TokenDto tokenDto = tokenProvider.generateToken(authentication);
		refreshToken = RefreshToken.builder()
			.refreshTokenKey(members.get(0).getId().toString())
			.refreshTokenValue(tokenDto.getRefreshToken())
			.build();
		refreshTokenMapper.insert(refreshToken);

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
	}
}
