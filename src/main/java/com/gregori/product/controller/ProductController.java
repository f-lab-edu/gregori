package com.gregori.product.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gregori.product.domain.Sorter;
import com.gregori.product.dto.ProductCreateDto;
import com.gregori.product.dto.ProductResponseDto;
import com.gregori.product.dto.ProductUpdateDto;
import com.gregori.product.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

	private final ProductService productService;

	@PostMapping
	public ResponseEntity<Void> createProduct(@RequestBody @Valid ProductCreateDto dto) {

		Long productId = productService.saveProduct(dto);

		return ResponseEntity.created(URI.create("/product/" + productId)).build();
	}

	@PutMapping
	public ResponseEntity<Void> updateProduct(@RequestBody @Valid ProductUpdateDto dto) {

		productService.updateProduct(dto);

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{productId}")
	public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long productId) {

		ProductResponseDto response = productService.getProduct(productId);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/search")
	public ResponseEntity<List<ProductResponseDto>> getProducts(
		@RequestParam String keyword,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "CREATED_AT_DESC") Sorter sorter) {

		List<ProductResponseDto> response = productService.getProductsByKeyword(keyword, page, sorter);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping
	public ResponseEntity<List<ProductResponseDto>> getProducts(
		@RequestParam(required = false) Long categoryId,
		@RequestParam(required = false) Long sellerId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "CREATED_AT_DESC") Sorter sorter) {

		if (categoryId == null && sellerId == null) {
			return ResponseEntity.badRequest().build();
		}

		List<ProductResponseDto> response;
		if (categoryId != null) {
			response = productService.getProductsByCategoryId(categoryId, page, sorter);
		} else {
			response = productService.getProductsBySellerId(sellerId, page, sorter);
		}

		return ResponseEntity.ok().body(response);
	}
}
