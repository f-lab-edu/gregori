package com.gregori.seller.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gregori.common.exception.BusinessRuleViolationException;
import com.gregori.common.exception.NotFoundException;
import com.gregori.item.domain.Item;
import com.gregori.item.mapper.ItemMapper;
import com.gregori.seller.domain.Seller;
import com.gregori.seller.dto.SellerRegisterDto;
import com.gregori.seller.dto.SellerResponseDto;
import com.gregori.seller.dto.SellerUpdateDto;
import com.gregori.seller.mapper.SellerMapper;

import lombok.RequiredArgsConstructor;

import static com.gregori.item.domain.Item.Status.ON_SALE;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
	private final SellerMapper sellerMapper;
	private final ItemMapper itemMapper;

	@Override
	public Long saveSeller(SellerRegisterDto sellerRegisterDto) {
		Seller seller = sellerRegisterDto.toEntity();
		sellerMapper.insert(seller);

		return seller.getId();
	}

	@Override
	public Long updateSeller(SellerUpdateDto sellerUpdateDto) {
		Seller seller = sellerMapper.findById(sellerUpdateDto.getId()).orElseThrow(NotFoundException::new);
		seller.updateSellerInfo(sellerUpdateDto.getBusinessNo(), sellerUpdateDto.getBusinessName());
		sellerMapper.update(seller);

		return seller.getId();
	}

	@Override
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
}
