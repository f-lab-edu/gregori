package com.gregori.seller.service;

import java.util.List;

import com.gregori.seller.dto.SellerRegisterDto;
import com.gregori.seller.dto.SellerResponseDto;
import com.gregori.seller.dto.SellerUpdateDto;

public interface SellerService {
	Long saveSeller(SellerRegisterDto sellerRegisterDto);
	Long updateSeller(SellerUpdateDto sellerUpdateDto);
	Long deleteSeller(Long sellerId);
	List<SellerResponseDto> getSellers(Long memberId);
	SellerResponseDto getSeller(Long memberId);
}
