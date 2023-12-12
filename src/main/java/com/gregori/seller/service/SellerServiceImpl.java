package com.gregori.seller.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.common.exception.BusinessRuleViolationException;
import com.gregori.common.exception.NotFoundException;
import com.gregori.common.exception.ValidationException;
import com.gregori.item.domain.Item;
import com.gregori.item.mapper.ItemMapper;
import com.gregori.seller.domain.Seller;
import com.gregori.seller.dto.SellerRegisterDto;
import com.gregori.seller.dto.SellerResponseDto;
import com.gregori.seller.dto.SellerUpdateDto;
import com.gregori.seller.mapper.SellerMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.gregori.item.domain.Item.Status.ON_SALE;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
	private final SellerMapper sellerMapper;
	private final ItemMapper itemMapper;

	@Override
	public Long saveSeller(SellerRegisterDto sellerRegisterDto) throws ValidationException {
		if (!businessNumberValidationCheck(sellerRegisterDto.getBusinessNumber())) {
			throw new ValidationException();
		}

		Seller seller = sellerRegisterDto.toEntity();
		sellerMapper.insert(seller);

		return seller.getId();
	}

	@Override
	@Transactional
	public Long updateSeller(SellerUpdateDto sellerUpdateDto) throws ValidationException {
		if (!businessNumberValidationCheck(sellerUpdateDto.getBusinessNumber())) {
			throw new ValidationException();
		}

		Seller seller = sellerMapper.findById(sellerUpdateDto.getId()).orElseThrow(NotFoundException::new);
		seller.updateSellerInfo(sellerUpdateDto.getBusinessNumber(), sellerUpdateDto.getBusinessName());
		sellerMapper.update(seller);

		return seller.getId();
	}

	@Override
	@Transactional
	public Long deleteSeller(Long sellerId) {
		List<Item> items = itemMapper.findBySellerId(sellerId);
		for (Item item: items) {
			if (item.getStatus() == ON_SALE) {
				throw new BusinessRuleViolationException("판매 중인 상품이 있으면 폐업 신청이 불가합니다.");
			}
		}

		Seller seller = sellerMapper.findById(sellerId).orElseThrow(NotFoundException::new);
		seller.closed();
		sellerMapper.update(seller);

		return seller.getId();
	}

	@Override
	public List<SellerResponseDto> getSellers(Long memberId) {
		List<Seller> sellers = sellerMapper.findByMemberId(memberId);

		return sellers.stream().map(seller -> new SellerResponseDto().toEntity(seller)).toList();
	}

	@Override
	public SellerResponseDto getSeller(Long sellerId) {
		Seller seller = sellerMapper.findById(sellerId).orElseThrow(NotFoundException::new);

		return new SellerResponseDto().toEntity(seller);
	}

	public boolean businessNumberValidationCheck(String businessNo) {
		String tenNumber = businessNo.replace("-", "");
		if (tenNumber.length() != 10) {
			throw new ValidationException();
		}

		String nineNumber = tenNumber.substring(0, 9);
		int lastNumber = Integer.parseInt(tenNumber.substring(9, 10));
		int[] multipliers = {1, 3, 7, 1, 3, 7, 1, 3, 5};
		int sum = 0;
		for (int i = 0; i <= 8; i++) {
			int number = Character.getNumericValue(nineNumber.charAt(i));
			sum += number*multipliers[i];
		}
		sum += (Integer.parseInt(nineNumber.substring(8, 9))*5) / 10;
		sum = sum % 10;
		int errorCheckingNumber = 10 - sum;

		return lastNumber == errorCheckingNumber;
	}
}
