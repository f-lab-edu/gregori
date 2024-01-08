package com.gregori.seller.controller;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregori.common.CustomWebMvcTest;
import com.gregori.member.domain.Member;
import com.gregori.member.domain.SessionMember;
import com.gregori.seller.dto.SellerRegisterDto;
import com.gregori.seller.dto.SellerUpdateDto;

import static com.gregori.auth.domain.Authority.SELLING_MEMBER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SellerControllerTest extends CustomWebMvcTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@DisplayName("판매자 생성을 요청하면 Created 응답을 반환한다.")
	void should_responseCreated_when_requestCreateSeller() throws Exception {

		// given
		SellerRegisterDto dto = new SellerRegisterDto("111-11-11111", "name");

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("member", new SessionMember(null, "a@a.a", SELLING_MEMBER));
		Member member = new Member("name", "a@a.a", "password");
		member.sellingMember();

		given(memberMapper.findById(null)).willReturn(Optional.of(member));

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.post("/seller")
				.session(session)
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)));

		// then
		actions.andExpect(status().isCreated());

		verify(sellerService).saveSeller(any(), refEq(dto));
	}

	@Test
	@DisplayName("판매자 수정을 요청하면 NoContent 응답을 반환한다.")
	void should_responseNoContent_when_requestUpdateSeller() throws Exception {

		// given
		SellerUpdateDto dto = new SellerUpdateDto(1L, "111-11-11111", "name");

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("member", new SessionMember(null, "a@a.a", SELLING_MEMBER));
		Member member = new Member("name", "a@a.a", "password");
		member.sellingMember();

		given(memberMapper.findById(null)).willReturn(Optional.of(member));

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.patch("/seller")
				.session(session)
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)));

		// then
		actions.andExpect(status().isNoContent());

		verify(sellerService).updateSeller(any(), refEq(dto));
	}

	@Test
	@DisplayName("판매자 삭제를 요청하면 NoContent 응답을 반환한다.")
	void should_responseNoContent_when_requestDeleteSeller() throws Exception {

		// given
		Long sellerId = 1L;

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("member", new SessionMember(null, "a@a.a", SELLING_MEMBER));
		Member member = new Member("name", "a@a.a", "password");
		member.sellingMember();

		given(memberMapper.findById(null)).willReturn(Optional.of(member));

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.delete("/seller/" + sellerId)
				.session(session)
				.contentType(APPLICATION_JSON));

		// then
		actions.andExpect(status().isNoContent());

		verify(sellerService).deleteSeller(null, sellerId);
	}

	@Test
	@DisplayName("판매자 조회를 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_requestGetSeller() throws Exception {

		// given
		Long sellerId = 1L;

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("member", new SessionMember(null, "a@a.a", SELLING_MEMBER));
		Member member = new Member("name", "a@a.a", "password");
		member.sellingMember();

		given(memberMapper.findById(null)).willReturn(Optional.of(member));

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.get("/seller/" + sellerId)
				.session(session)
				.contentType(APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk());

		verify(sellerService).getSeller(null, sellerId);
	}

	@Test
	@DisplayName("판매자 목록 조회를 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_requestGetSellers() throws Exception {

		// given
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("member", new SessionMember(null, "a@a.a", SELLING_MEMBER));
		Member member = new Member("name", "a@a.a", "password");
		member.sellingMember();

		given(memberMapper.findById(null)).willReturn(Optional.of(member));

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.get("/seller?page=1")
				.session(session)
				.contentType(APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk());

		verify(sellerService).getSellers(null, 1);
	}
}
