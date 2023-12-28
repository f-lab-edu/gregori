package com.gregori.product.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gregori.common.CustomWebMvcTest;
import com.gregori.product.domain.Sorter;
import com.gregori.product.dto.ProductCreateDto;
import com.gregori.product.dto.ProductUpdateDto;

import static com.gregori.product.domain.Product.Status.PRE_SALE;
import static com.gregori.product.domain.Sorter.CREATED_AT_DESC;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest extends CustomWebMvcTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@DisplayName("상품 등록을 요청하면 Created 응답을 반환한다.")
	void should_responseCreated_when_requestCreateProduct() throws Exception {

		// given
		ProductCreateDto dto = new ProductCreateDto(1L, "name", 1L, 1L);

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.post("/product")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)));

		// then
		actions.andExpect(status().isCreated()).andDo(print());

		verify(productService).saveProduct(refEq(dto));
	}

	@Test
	@DisplayName("상품 수정을 요청하면 NoContent 응답을 반환한다.")
	void should_responseNoContent_when_requestUpdateProduct() throws Exception {

		// given
		ProductUpdateDto dto = new ProductUpdateDto(1L, 1L, "name", 1L, 1L, PRE_SALE);

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.put("/product")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)));

		// then
		actions.andExpect(status().isNoContent()).andDo(print());

		verify(productService).updateProduct(refEq(dto));
	}

	@Test
	@DisplayName("상품 삭제를 요청하면 NoContent 응답을 반환한다.")
	void should_responseNoContent_when_requestDeleteProduct() throws Exception {

		// given
		Long productId = 1L;

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.delete("/product/" + productId)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isNoContent()).andDo(print());

		verify(productService).deleteProduct(productId);
	}

	@Test
	@DisplayName("상품 조회를 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_request_getProduct() throws Exception {

		// given
		Long productId = 1L;

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.get("/product/" + productId)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(productService).getProduct(productId);
	}

	@Test
	@DisplayName("검색어와 함께 상품 목록 조회를 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_request_getProductsWithKeyword() throws Exception {

		// given
		String keyword = "스프링";
		int page = 1;
		Sorter sorter = CREATED_AT_DESC;

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.get("/product?keyword=" + keyword + "&page=" + page + "&sorter=" + sorter)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(productService).getProducts(keyword, null, null, page, sorter);
	}

	@Test
	@DisplayName("categoryId와 함께 상품 목록 조회를 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_request_getProductsWithCategoryId() throws Exception {

		// given
		Long categoryId = 1L;
		int page = 1;
		Sorter sorter = CREATED_AT_DESC;

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.get("/product?categoryId=" + categoryId + "&page=" + page + "&sorter=" + sorter)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(productService).getProducts(null, categoryId, null, page, sorter);
	}

	@Test
	@DisplayName("sellerId와 함께 상품 목록 조회를 요청하면 Ok 응답을 반환한다.")
	void should_responseOk_when_request_getProductsWithSellerId() throws Exception {

		// given
		Long sellerId = 1L;
		int page = 1;
		Sorter sorter = CREATED_AT_DESC;

		// when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders.get("/product?&sellerId=" + sellerId + "&page=" + page + "&sorter=" + sorter)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(productService).getProducts(null, null, sellerId, page, sorter);
	}
}
