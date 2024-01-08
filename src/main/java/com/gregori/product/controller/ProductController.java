package com.gregori.product.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gregori.auth.domain.CurrentMember;
import com.gregori.auth.domain.LoginCheck;
import com.gregori.member.domain.SessionMember;
import com.gregori.product.domain.Sorter;
import com.gregori.product.dto.ProductCreateDto;
import com.gregori.product.dto.ProductResponseDto;
import com.gregori.product.dto.ProductUpdateDto;
import com.gregori.product.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static com.gregori.auth.domain.Authority.SELLING_MEMBER;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

	private final ProductService productService;

	@LoginCheck(SELLING_MEMBER)
	@PostMapping
	public ResponseEntity<Void> createProduct(@RequestBody @Valid ProductCreateDto dto) {

		Long productId = productService.saveProduct(dto);

		return ResponseEntity.created(URI.create("/product/" + productId)).build();
	}

	@LoginCheck(SELLING_MEMBER)
	@PutMapping
	public ResponseEntity<Void> updateProduct(
		@CurrentMember SessionMember sessionMember, @RequestBody @Valid ProductUpdateDto dto) {

		productService.updateProduct(sessionMember.getId(), dto);

		return ResponseEntity.noContent().build();
	}

	@LoginCheck(SELLING_MEMBER)
	@DeleteMapping("/{productId}")
	public ResponseEntity<Void> deleteProduct(
		@CurrentMember SessionMember sessionMember, @PathVariable Long productId) {

		productService.deleteProduct(sessionMember.getId(), productId);

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{productId}")
	public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long productId) {

		ProductResponseDto response = productService.getProduct(productId);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping
	public ResponseEntity<List<ProductResponseDto>> getProducts(
		@RequestParam(required = false) String keyword,
		@RequestParam(required = false) Long categoryId,
		@RequestParam(required = false) Long sellerId,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "CREATED_AT_DESC") Sorter sorter) {

		List<ProductResponseDto> response = productService.getProducts(keyword, categoryId, sellerId, page, sorter);

		return ResponseEntity.ok().body(response);
	}
}
