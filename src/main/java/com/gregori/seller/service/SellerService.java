package com.gregori.seller.service;

import java.util.List;

import com.gregori.common.exception.NotFoundException;
import com.gregori.common.exception.ValidationException;
import com.gregori.seller.dto.SellerRegisterDto;
import com.gregori.seller.dto.SellerResponseDto;
import com.gregori.seller.dto.SellerUpdateDto;

public interface SellerService {

	Long saveSeller(SellerRegisterDto sellerRegisterDto) throws ValidationException;
	void updateSeller(SellerUpdateDto sellerUpdateDto) throws ValidationException;
	void deleteSeller(Long sellerId) throws NotFoundException;
	SellerResponseDto getSeller(Long sellerId) throws NotFoundException;
	List<SellerResponseDto> getSellers(Long memberId);
}
