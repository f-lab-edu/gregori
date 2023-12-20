package com.gregori.seller.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gregori.seller.dto.SellerRegisterDto;
import com.gregori.seller.service.SellerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

	private final SellerService sellerService;

	@PostMapping("/register")
	public ResponseEntity<Long> createSeller(@RequestBody @Valid SellerRegisterDto sellerRegisterDto) {

		Long sellerId = sellerService.saveSeller(sellerRegisterDto);

		return ResponseEntity.created(URI.create("/seller/" + sellerId)).build();
	}
}
