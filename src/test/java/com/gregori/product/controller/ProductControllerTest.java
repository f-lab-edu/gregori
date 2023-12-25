package com.gregori.product.controller;

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
import com.gregori.product.domain.Sorter;
import com.gregori.product.dto.ProductCreateDto;
import com.gregori.product.dto.ProductUpdateDto;
import com.gregori.product.service.ProductService;

import static com.gregori.product.domain.Product.Status.PRE_SALE;
import static com.gregori.product.domain.Sorter.CREATED_AT_DESC;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = ProductController.class)
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;


	@MockBean
	ProductService productService;

	@Test
	@DisplayName("상품 등록을 요청하면 Created 응답을 반환한다.")
	void should_responseCreated_when_requestCreateProduct() throws Exception {

		// given
		ProductCreateDto dto = new ProductCreateDto(1L, "name", 1L, 1L);

		// when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/product")
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
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.put("/product")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(dto)));

		// then
		actions.andExpect(status().isNoContent()).andDo(print());

		verify(productService).updateProduct(refEq(dto));
	}

	@Test
	@DisplayName("상품 조회를 요청하면 OK 응답을 반환한다.")
	void should_responseOk_when_request_getProduct() throws Exception {

		// given
		Long productId = 1L;

		// when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/product/" + productId)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(productService).getProduct(productId);
	}

	@Test
	@DisplayName("검색어와 함께 상품 목록 조회를 요청하면 OK 응답을 반환한다.")
	void should_responseOk_when_request_getProductsWithKeyword() throws Exception {

		// given
		String keyword = "스프링";
		Sorter sorter = CREATED_AT_DESC;
		int page = 1;

		// when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
			.get("/product/search?keyword=" + keyword + "&page=" + page + "&sorter=" + sorter)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(productService).getProductsByKeyword(keyword, page, sorter);
	}

	@Test
	@DisplayName("카테고리 id와 함께 상품 목록 조회를 요청하면 OK 응답을 반환한다.")
	void should_responseOk_when_request_getProductsWithCategoryId() throws Exception {

		// given
		Long categoryId = 1L;
		Sorter sorter = CREATED_AT_DESC;
		int page = 1;

		// when
		ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
			.get("/product/category?categoryId=" + 1L + "&page=" + page + "&sorter=" + sorter)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON));

		// then
		actions.andExpect(status().isOk()).andDo(print());

		verify(productService).getProductsByCategory(categoryId, page, sorter);
	}
}
