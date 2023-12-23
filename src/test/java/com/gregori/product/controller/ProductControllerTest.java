package com.gregori.product.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.gregori.product.service.ProductService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = ProductController.class)
class ProductControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	ProductService productService;

	@Test
	@DisplayName("클라이언트의 요청에 따라 테이블에 저장된 상품을 조회한다.")
	void should_responseSuccess_when_request_getProduct() throws Exception {

		// given
		Long productId = 1L;

		// when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/product/" + productId)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(productService).getProduct(productId);
	}
}
