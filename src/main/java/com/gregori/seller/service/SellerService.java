package com.gregori.seller.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.common.exception.BusinessRuleViolationException;
import com.gregori.common.exception.NotFoundException;
import com.gregori.common.exception.UnauthorizedException;
import com.gregori.common.exception.ValidationException;
import com.gregori.member.domain.SessionMember;
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
import static com.gregori.auth.domain.Authority.SELLING_MEMBER;
import static com.gregori.common.domain.IsDeleted.FALSE;
import static com.gregori.product.domain.Sorter.CREATED_AT_DESC;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellerService {

	private final MemberMapper memberMapper;
	private final SellerMapper sellerMapper;
	private final ProductMapper productMapper;

	@Transactional
	public Long saveSeller(SessionMember sessionMember, SellerRegisterDto dto) throws ValidationException {

		checkBusinessNumberValidation(dto.getBusinessNumber());

		if (sessionMember.getAuthority() == GENERAL_MEMBER) {
			memberMapper.updateAuthority(sessionMember.getId(), SELLING_MEMBER);
		}

		Seller seller = dto.toEntity(sessionMember.getId());
		sellerMapper.insert(seller);

		return seller.getId();
	}

	@Transactional
	public void updateSeller(Long memberId, SellerUpdateDto dto) throws ValidationException {

		checkBusinessNumberValidation(dto.getBusinessNumber());

		Seller seller = sellerMapper.findById(dto.getId()).orElseThrow(NotFoundException::new);
		if (!Objects.equals(memberId, seller.getMemberId())) {
			throw new UnauthorizedException("요청한 회원과 판매자가 일치하지 않습니다.");
		}

		seller.updateSellerInfo(dto.getBusinessNumber(), dto.getBusinessName());
		sellerMapper.update(seller);
	}

	@Transactional
	public void deleteSeller(Long memberId, Long sellerId) throws NotFoundException {

		Seller seller = sellerMapper.findById(sellerId).orElseThrow(NotFoundException::new);
		if (!Objects.equals(memberId, seller.getMemberId())) {
			throw new UnauthorizedException("요청한 회원과 판매자가 일치하지 않습니다.");
		}

		List<Product> products = productMapper.find(null, null, sellerId, null, null, CREATED_AT_DESC.toString())
			.stream().filter(product -> product.getIsDeleted() == FALSE).toList();
		if (!products.isEmpty()) {
			throw new BusinessRuleViolationException("상품이 있으면 폐업 신청이 불가합니다.");
		}

		seller.isDeletedTrue();
		sellerMapper.updateIsDeleted(sellerId, seller.getIsDeleted());
	}

	public SellerResponseDto getSeller(Long memberId, Long sellerId) throws NotFoundException {

		Seller seller = sellerMapper.findById(sellerId).orElseThrow(NotFoundException::new);
		if (!Objects.equals(memberId, seller.getMemberId())) {
			throw new UnauthorizedException("요청한 회원과 판매자가 일치하지 않습니다.");
		}

		return new SellerResponseDto().toEntity(seller);
	}

	public List<SellerResponseDto> getSellers(Long memberId, int page) {

		int limit = 10;
		int offset = (page - 1) * limit;

		List<Seller> sellers = sellerMapper.findByMemberId(memberId, limit, offset);

		return sellers.stream().map(seller -> new SellerResponseDto().toEntity(seller)).toList();
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
