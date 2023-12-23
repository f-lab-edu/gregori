package com.gregori.seller.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregori.seller.dto.SellerRegisterDto;
import com.gregori.seller.service.SellerService;

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
	@DisplayName("판매자 생성을 요청하면 OK 응답을 반환한다.")
	void should_responseOK_when_requestCreateSeller() throws Exception {

		// given
		SellerRegisterDto dto = new SellerRegisterDto(1L, "111-11-11111", "name");

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.post("/seller/register")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))
		);

		// then
		actions.andExpect(status().isCreated()).andDo(print());
	}
}
