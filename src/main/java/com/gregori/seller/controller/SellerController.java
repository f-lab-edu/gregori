package com.gregori.seller.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gregori.seller.dto.SellerRegisterDto;
import com.gregori.seller.dto.SellerResponseDto;
import com.gregori.seller.dto.SellerUpdateDto;
import com.gregori.seller.service.SellerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static java.lang.Long.parseLong;

@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

	private final SellerService sellerService;

	@PostMapping
	public ResponseEntity<Long> createSeller(@RequestBody @Valid SellerRegisterDto sellerRegisterDto) {

		Long sellerId = sellerService.saveSeller(sellerRegisterDto);

		return ResponseEntity.created(URI.create("/seller/" + sellerId)).build();
	}

	@PatchMapping
	public ResponseEntity<Void> updateSeller(@RequestBody @Valid SellerUpdateDto sellerUpdateDto) {

		sellerService.updateSeller(sellerUpdateDto);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{sellerId}")
	public ResponseEntity<Void> deleteSeller(@PathVariable Long sellerId) {

		sellerService.deleteSeller(sellerId);

		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<List<SellerResponseDto>> getSellers() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		long memberId = parseLong(authentication.getName());

		List<SellerResponseDto> response = sellerService.getSellers(memberId);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/{sellerId}")
	public ResponseEntity<SellerResponseDto> getSeller(@PathVariable Long sellerId) {

		SellerResponseDto response = sellerService.getSeller(sellerId);

		return ResponseEntity.ok().body(response);
	}
}
