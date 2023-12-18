package com.gregori.seller.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gregori.common.response.CustomResponse;
import com.gregori.seller.dto.SellerRegisterDto;
import com.gregori.seller.service.SellerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static com.gregori.common.response.SuccessMessage.REGISTER;

@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {
	private final SellerService sellerService;

	@PostMapping("/register")
	public ResponseEntity<CustomResponse<Long>> createSeller(@RequestBody @Valid SellerRegisterDto sellerRegisterDto) {
		CustomResponse<Long> response = CustomResponse
			.success(sellerService.saveSeller(sellerRegisterDto), REGISTER);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
