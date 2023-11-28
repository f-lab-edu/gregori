package com.gregori.controller.member;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class MemberControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("회원가입 테스트")
	void register() throws Exception {
		// given
		Map<String, String> input = new HashMap<>();
		input.put("name", "일호");
		input.put("email", "a@a.a");
		input.put("password", "aa11111!");

		// when
		ResultActions actions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/member/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input))
		);

		// then
		actions.andExpect(status().isOk())
			.andExpect(jsonPath("$.result", is("SUCCESS")))
			.andExpect(jsonPath("$.data", is(notNullValue())))
			.andExpect(jsonPath("$.data.id", is(notNullValue())))
			.andExpect(jsonPath("$.data.email", is(input.get("email"))))
			.andExpect(jsonPath("$.data.name", is(input.get("name"))))
			.andExpect(jsonPath("$.data.status", is("ACTIVATE")))
			.andExpect(jsonPath("$.description", is(notNullValue())))
			.andDo(print());

		actions.andDo(document("member-register",
			requestFields(
				fieldWithPath("name").description("회원 이름"),
				fieldWithPath("email").description("회원 이메일"),
				fieldWithPath("password").description("회원 비밀번호")
			), responseFields(
				fieldWithPath("result").description("요청에 대한 응답 결과"),
				fieldWithPath("data").description("요청에 대한 데이터"),
				fieldWithPath("data.id").description("가입한 회원 인덱스"),
				fieldWithPath("data.email").description("가입한 회원 이메일"),
				fieldWithPath("data.name").description("가입한 회원 이름"),
				fieldWithPath("data.status").description("가입한 회원 상태"),
				fieldWithPath("errorType").description("에러가 발생한 경우 에러 타입"),
				fieldWithPath("description").description("응답에 대한 설명")
			)
		));
	}
}
