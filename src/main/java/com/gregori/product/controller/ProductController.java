package com.gregori.product.controller;

import static com.gregori.common.response.SuccessMessage.GET;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gregori.common.response.CustomResponse;
import com.gregori.product.dto.ProductResponseDto;
import com.gregori.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
	private final ProductService productService;

	@GetMapping("/{productId}")
	public ResponseEntity<CustomResponse<ProductResponseDto>> getProduct(@PathVariable Long productId) {
		CustomResponse<ProductResponseDto> response = CustomResponse
			.success(productService.getProduct(productId), GET);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
