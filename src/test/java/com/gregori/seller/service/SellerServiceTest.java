package com.gregori.seller.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gregori.common.exception.BusinessRuleViolationException;
import com.gregori.common.exception.NotFoundException;
import com.gregori.common.exception.UnauthorizedException;
import com.gregori.common.exception.ValidationException;
import com.gregori.member.domain.Member;
import com.gregori.member.domain.SessionMember;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.product.domain.Product;
import com.gregori.product.mapper.ProductMapper;
import com.gregori.seller.domain.Seller;
import com.gregori.seller.dto.SellerRegisterDto;
import com.gregori.seller.dto.SellerUpdateDto;
import com.gregori.seller.mapper.SellerMapper;

import static com.gregori.auth.domain.Authority.SELLING_MEMBER;
import static com.gregori.common.domain.IsDeleted.TRUE;
import static com.gregori.product.domain.Sorter.CREATED_AT_DESC;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {

	@Mock
	private MemberMapper memberMapper;

	@Mock
	private SellerMapper sellerMapper;

	@Mock
	private ProductMapper productMapper;

	@InjectMocks
	private SellerService sellerService;

	@Test
	@DisplayName("판매자 생성을 성공하면 id를 반환한다.")
	void should_returnId_when_saveSellerSuccess() {

		// given
		SessionMember sessionMember = new SessionMember(null, "email", SELLING_MEMBER);
		SellerRegisterDto dto = new SellerRegisterDto("123-45-67891", "name");

		// when
		sellerService.saveSeller(sessionMember, dto);

		// then
		verify(sellerMapper).insert(any(Seller.class));
	}

	@Test
	@DisplayName("판매자 정보 갱신을 성공하면 id를 반환한다.")
	void should_returnId_when_updateSellerSuccess() {

		// given
		SellerUpdateDto sellerUpdateDto = new SellerUpdateDto(1L, "123-45-67891", "name");
		Seller seller = new Seller(1L, "123-45-67891", "name");

		given(sellerMapper.findById(1L)).willReturn(Optional.of(seller));

		// when
		sellerService.updateSeller(1L, sellerUpdateDto);

		// then
		verify(sellerMapper).update(any(Seller.class));
	}

	@Test
	@DisplayName("잘못된 사업자 등록번호면 판매자 생성과 갱신을 실패한다.")
	void should_ValidationException_when_invalidBusinessNumber() {

		// given
		SellerRegisterDto dto1 = new SellerRegisterDto("111-11-11111", "name");
		SellerUpdateDto dto2 = new SellerUpdateDto(1L, "111-11-11111", "name");

		// when, then
		assertThrows(ValidationException.class, () -> sellerService.saveSeller(null, dto1));
		assertThrows(ValidationException.class, () -> sellerService.updateSeller(null, dto2));
	}

	@Test
	@DisplayName("판매자 삭제를 성공하면 id를 반환한다.")
	void should_returnId_when_deleteSellerSuccess() {

		// given
		Long sellerId = 1L;

		given(sellerMapper.findById(sellerId)).willReturn(Optional.of(new Seller()));
		given(productMapper.find(null, null, sellerId, null, null, CREATED_AT_DESC.name())).willReturn(List.of());

		// when
		sellerService.deleteSeller(null, sellerId);

		// then
		verify(sellerMapper).updateIsDeleted(sellerId, TRUE);
	}

	@Test
	@DisplayName("삭제되지 않은 상품이 있으면 폐업 신청을 실패한다.")
	void should_BusinessRuleViolationException_when_productIsDeletedFalse() {

		// given
		Long sellerId = 1L;
		Product product = new Product(1L, 1L, "name", 1L, 1L);

		given(sellerMapper.findById(sellerId)).willReturn(Optional.of(new Seller()));
		given(productMapper.find(null, null, sellerId, null, null, CREATED_AT_DESC.toString())).willReturn(List.of(product));

		// when, then
		assertThrows(BusinessRuleViolationException.class, () -> sellerService.deleteSeller(null, sellerId));
	}

	@Test
	@DisplayName("판매자 조회를 성공하면 판매자를 반환한다.")
	void should_returnSeller_when_getSellerSuccess() {

		// given
		Long sellerId = 1L;

		given(sellerMapper.findById(sellerId))
			.willReturn(Optional.of(new Seller()))
			.willReturn(Optional.of(new Seller()));

		// when
		sellerService.getSeller(null, sellerId);

		// then
		verify(sellerMapper).findById(sellerId);
	}

	@Test
	@DisplayName("판매자 조회를 실패하면 에러가 발생한다.")
	void should_NotFoundException_when_findSellerFailure() {

		// given
		SellerUpdateDto dto = new SellerUpdateDto(1L, "123-45-67891", "name");

		given(sellerMapper.findById(1L)).willReturn(Optional.empty());

		// when, then
		assertThrows(NotFoundException.class, () -> sellerService.updateSeller(null, dto));
		assertThrows(NotFoundException.class, () -> sellerService.deleteSeller(1L, 1L));
		assertThrows(NotFoundException.class, () -> sellerService.getSeller(1L, 1L));
	}

	@Test
	@DisplayName("판매자 목록 조회를 성공하면 판매자 목록을 반환한다.")
	void should_returnSellers_when_getSellersSuccess() {

		// given
		Long sellerId = 1L;

		given(sellerMapper.findByMemberId(sellerId, 10, 0)).willReturn(List.of(new Seller()));

		// when
		sellerService.getSellers(sellerId, 1);

		// then
		verify(sellerMapper).findByMemberId(sellerId, 10, 0);
	}

	@Test
	@DisplayName("세션의 회원 id와 판매자의 회원 id가 다르면 에러가 발생한다.")
	void should_UnauthorizedException_when_invalidMemberId() {

		// given
		Long sellerId = 1L;

		given(sellerMapper.findById(sellerId)).willReturn(Optional.of(new Seller()));

		// when, then
		assertThrows(UnauthorizedException.class, () -> sellerService.deleteSeller(1L, 1L));
		assertThrows(UnauthorizedException.class, () -> sellerService.getSeller(1L, 1L));
	}
}
