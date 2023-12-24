package com.gregori.seller.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregori.seller.dto.SellerRegisterDto;
import com.gregori.seller.dto.SellerUpdateDto;
import com.gregori.seller.service.SellerService;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = SellerController.class)
class SellerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	SellerService sellerService;

	@Test
	@DisplayName("판매자 생성을 요청하면 CREATED 응답을 반환한다.")
	void should_responseCreated_when_requestCreateSeller() throws Exception {

		// given
		SellerRegisterDto dto = new SellerRegisterDto(1L, "111-11-11111", "name");

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.post("/seller")
				.with(csrf())
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)));

		// then
		actions.andExpect(status().isCreated()).andDo(print());

		verify(sellerService).saveSeller(refEq(dto));
	}

	@Test
	@DisplayName("판매자 수정을 요청하면 NoContent 응답을 반환한다.")
	void should_responseNoContent_when_requestUpdateSeller() throws Exception {

		// given
		SellerUpdateDto dto = new SellerUpdateDto(1L, 1L, "111-11-11111", "name");

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.patch("/seller")
				.with(csrf())
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)));

		// then
		actions.andExpect(status().isNoContent()).andDo(print());

		verify(sellerService).updateSeller(refEq(dto));
	}

	@Test
	@DisplayName("판매자 삭제를 요청하면 NoContent 응답을 반환한다.")
	void should_responseNoContent_when_requestDeleteSeller() throws Exception {

		// given
		Long sellerId = 1L;

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.delete("/seller/" + sellerId)
				.with(csrf())
				.contentType(APPLICATION_JSON));

		// then
		actions.andExpect(status().isNoContent()).andDo(print());

		verify(sellerService).deleteSeller(sellerId);
	}

	@Test
	@DisplayName("판매자 목록 조회를 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_requestGetSellers() throws Exception {

		// given
		Long memberId = 1L;

		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);

		given(securityContext.getAuthentication()).willReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		given(authentication.getName()).willReturn(memberId.toString());

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.get("/seller")
				.with(csrf())
				.contentType(APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(sellerService).getSellers(memberId);
	}

	@Test
	@DisplayName("판매자 조회를 요청하면 OK 응답을 반환한다.")
	void should_responseOk_when_requestGetSeller() throws Exception {

		// given
		Long sellerId = 1L;

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.get("/seller/" + sellerId)
				.with(csrf())
				.contentType(APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(sellerService).getSeller(sellerId);
	}
}
