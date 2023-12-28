package com.gregori.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.gregori.auth.service.AuthService;
import com.gregori.category.service.CategoryService;
import com.gregori.member.service.MemberService;
import com.gregori.order.service.OrderService;
import com.gregori.product.service.ProductService;
import com.gregori.seller.service.SellerService;

@WebMvcTest
public abstract class CustomWebMvcTest {

	@Autowired
	protected MockMvc mockMvc;

	@MockBean
	protected AuthService authService;

	@MockBean
	protected CategoryService categoryService;

	@MockBean
	protected MemberService memberService;

	@MockBean
	protected OrderService orderService;

	@MockBean
	protected ProductService productService;

	@MockBean
	protected SellerService sellerService;
}
