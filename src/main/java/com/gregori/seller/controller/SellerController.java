package com.gregori.seller.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gregori.auth.domain.CurrentMember;
import com.gregori.auth.domain.LoginCheck;
import com.gregori.member.domain.SessionMember;
import com.gregori.seller.dto.SellerRegisterDto;
import com.gregori.seller.dto.SellerResponseDto;
import com.gregori.seller.dto.SellerUpdateDto;
import com.gregori.seller.service.SellerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static com.gregori.auth.domain.Authority.GENERAL_MEMBER;
import static com.gregori.auth.domain.Authority.SELLING_MEMBER;

@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

	private final SellerService sellerService;

	@LoginCheck({GENERAL_MEMBER, SELLING_MEMBER })
	@PostMapping
	public ResponseEntity<Void> createSeller(@RequestBody @Valid SellerRegisterDto dto) {

		Long sellerId = sellerService.saveSeller(dto);

		return ResponseEntity.created(URI.create("/seller/" + sellerId)).build();
	}

	@LoginCheck(SELLING_MEMBER)
	@PatchMapping
	public ResponseEntity<Void> updateSeller(@RequestBody @Valid SellerUpdateDto dto) {

		sellerService.updateSeller(dto);

		return ResponseEntity.noContent().build();
	}

	@LoginCheck(SELLING_MEMBER)
	@DeleteMapping("/{sellerId}")
	public ResponseEntity<Void> deleteSeller(@PathVariable Long sellerId) {

		sellerService.deleteSeller(sellerId);

		return ResponseEntity.noContent().build();
	}

	@LoginCheck(SELLING_MEMBER)
	@GetMapping("/{sellerId}")
	public ResponseEntity<SellerResponseDto> getSeller(@PathVariable Long sellerId) {

		SellerResponseDto response = sellerService.getSeller(sellerId);

		return ResponseEntity.ok().body(response);
	}

	@LoginCheck(SELLING_MEMBER)
	@GetMapping
	public ResponseEntity<List<SellerResponseDto>> getSellers(@CurrentMember SessionMember sessionMember,
		@RequestParam(defaultValue = "1") int page) {

		List<SellerResponseDto> response = sellerService.getSellers(sessionMember.getId(), page);

		return ResponseEntity.ok().body(response);
	}
}
