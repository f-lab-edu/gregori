package com.gregori.seller.controller;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregori.common.response.CustomResponse;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.seller.mapper.SellerMapper;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class SellerControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private SellerMapper sellerMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	Member member;
	Long sellerId = -1L;

	@BeforeEach
	void beforeEach() {
		member = Member.builder()
			.email("a@a.a")
			.name("일호")
			.password(passwordEncoder.encode("aa11111!"))
			.build();
		memberMapper.insert(member);
	}

	@AfterEach
	void afterEach() {
		if (sellerId > -1) {
			sellerMapper.deleteByIds(List.of(sellerId));
			sellerId = -1L;
		}
		if(member != null) {
			memberMapper.deleteById(member.getId());
			member = null;
		}
	}

	@Test
	@DisplayName("클라이언트의 요청에 따라 신규 셀러를 등록한다.")
	void createSeller() throws Exception {
		// given
		Map<String, String> input = new HashMap<>();
		input.put("memberId", member.getId().toString());
		input.put("businessNumber", "123-45-67891");
		input.put("businessName", "일호 상점");

		// when
		ResultActions actions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/seller/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input))
		);

		CustomResponse<Long> result = objectMapper.readValue(
			actions.andReturn().getResponse().getContentAsString(),
			new TypeReference<>(){});
		sellerId = result.getData();

		// then
		actions.andExpect(status().isOk())
			.andExpect(jsonPath("$.result", is("SUCCESS")))
			.andExpect(jsonPath("$.httpStatus", is("OK")))
			.andExpect(jsonPath("$.data", is(notNullValue())))
			.andExpect(jsonPath("$.description", is(notNullValue())))
			.andDo(print());
	}
}
