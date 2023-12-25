package com.gregori.seller.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.common.exception.BusinessRuleViolationException;
import com.gregori.common.exception.NotFoundException;
import com.gregori.common.exception.ValidationException;
import com.gregori.product.domain.Product;
import com.gregori.product.mapper.ProductMapper;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.seller.domain.Seller;
import com.gregori.seller.dto.SellerRegisterDto;
import com.gregori.seller.dto.SellerResponseDto;
import com.gregori.seller.dto.SellerUpdateDto;
import com.gregori.seller.mapper.SellerMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.gregori.auth.domain.Authority.GENERAL_MEMBER;
import static com.gregori.product.domain.Product.Status.ON_SALE;
import static com.gregori.product.domain.Sorter.CREATED_AT_DESC;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

	private final MemberMapper memberMapper;
	private final SellerMapper sellerMapper;
	private final ProductMapper productMapper;

	@Override
	@Transactional
	public Long saveSeller(SellerRegisterDto sellerRegisterDto) throws ValidationException {

		checkBusinessNumberValidation(sellerRegisterDto.getBusinessNumber());

		Member member = memberMapper.findById(sellerRegisterDto.getMemberId()).orElseThrow(NotFoundException::new);
		if (member.getAuthority() == GENERAL_MEMBER) {
			member.sellingMember();
			memberMapper.updateAuthority(member.getId(), member.getAuthority());
		}

		Seller seller = sellerRegisterDto.toEntity();
		sellerMapper.insert(seller);

		return seller.getId();
	}

	@Override
	@Transactional
	public void updateSeller(SellerUpdateDto sellerUpdateDto) throws ValidationException {

		checkBusinessNumberValidation(sellerUpdateDto.getBusinessNumber());

		Seller seller = sellerMapper.findById(sellerUpdateDto.getId()).orElseThrow(NotFoundException::new);
		seller.updateSellerInfo(sellerUpdateDto.getBusinessNumber(), sellerUpdateDto.getBusinessName());
		sellerMapper.update(seller);
	}

	@Override
	@Transactional
	public void deleteSeller(Long sellerId) throws NotFoundException {

		List<Product> products = productMapper.find(null, null, sellerId, null, null, CREATED_AT_DESC.toString());
		for (Product product : products) {
			if (product.getStatus() == ON_SALE) {
				throw new BusinessRuleViolationException("판매 중인 상품이 있으면 폐업 신청이 불가합니다.");
			}
		}

		Seller seller = sellerMapper.findById(sellerId).orElseThrow(NotFoundException::new);
		seller.closed();
		sellerMapper.update(seller);
	}

	@Override
	public List<SellerResponseDto> getSellers(Long memberId) {

		List<Seller> sellers = sellerMapper.findByMemberId(memberId);

		return sellers.stream().map(seller -> new SellerResponseDto().toEntity(seller)).toList();
	}

	@Override
	public SellerResponseDto getSeller(Long sellerId) throws NotFoundException {

		Seller seller = sellerMapper.findById(sellerId).orElseThrow(NotFoundException::new);

		return new SellerResponseDto().toEntity(seller);
	}

	private void checkBusinessNumberValidation(String businessNumber) {

		String tenNumber = businessNumber.replace("-", "");
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

		if (lastNumber != errorCheckingNumber) {
			throw new ValidationException();
		}
	}
}
