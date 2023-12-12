package com.gregori.seller.service;

import java.util.List;

import com.gregori.common.exception.ValidationException;
import com.gregori.seller.dto.SellerRegisterDto;
import com.gregori.seller.dto.SellerResponseDto;
import com.gregori.seller.dto.SellerUpdateDto;

public interface SellerService {
	Long saveSeller(SellerRegisterDto sellerRegisterDto) throws ValidationException;
	Long updateSeller(SellerUpdateDto sellerUpdateDto) throws ValidationException;
	Long deleteSeller(Long sellerId);
	List<SellerResponseDto> getSellers(Long memberId);
	SellerResponseDto getSeller(Long sellerId);
}
