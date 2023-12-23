package com.gregori.seller.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gregori.common.exception.AccessDeniedException;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.product.mapper.ProductMapper;
import com.gregori.seller.domain.Seller;
import com.gregori.seller.dto.SellerRegisterDto;
import com.gregori.seller.dto.SellerUpdateDto;
import com.gregori.seller.mapper.SellerMapper;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SellerServiceImplTest {

	@Mock
	private MemberMapper memberMapper;

	@Mock
	private SellerMapper sellerMapper;

	@Mock
	private ProductMapper productMapper;

	@InjectMocks
	private SellerServiceImpl sellerService;

	@Test
	@DisplayName("판매자 생성을 성공하면 id를 반환한다.")
	void should_returnId_when_saveSellerSuccess() {

		// given
		SellerRegisterDto dto = new SellerRegisterDto(1L, "123-45-67891", "businessName");
		Member member = new Member("name", "email", "password");

		given(memberMapper.findById(1L)).willReturn(Optional.of(member));

		// when
		sellerService.saveSeller(dto);

		// then
		verify(sellerMapper).insert(any(Seller.class));
	}

	@Test
	@DisplayName("판매자 정보 갱신을 성공하면 id를 반환한다.")
	void should_returnId_when_updateSellerSuccess() {

		// given
		SellerUpdateDto sellerUpdateDto = new SellerUpdateDto(1L, 1L, "123-45-67891", "name");
		Seller seller = new Seller(1L, "123-45-67891", "name");

		given(sellerMapper.findById(1L)).willReturn(Optional.of(seller));

		// when
		sellerService.updateSeller(sellerUpdateDto);

		// then
		verify(sellerMapper).update(any(Seller.class));
	}

	@Test
	@DisplayName("잘못된 사업자 등록번호면 판매자 생성을 실패한다.")
	void should_ValidationException_when_invalidBusinessNumber() {

		// given
		SellerRegisterDto dto = new SellerRegisterDto(1L, "111-11-11111", "businessName");
		SellerUpdateDto sellerUpdateDto = new SellerUpdateDto(1L, 1L, "111-11-11111", "name");

		// when, then
		assertThrows(AccessDeniedException.class, () -> sellerService.saveSeller(dto));
		assertThrows(AccessDeniedException.class, () -> sellerService.updateSeller(sellerUpdateDto));
	}

	@Test
	@DisplayName("판매자 삭제를 성공하면 id를 반환한다.")
	void should_returnId_when_deleteSellerSuccess() {

		// given
		Long sellerId = 1L;
		Seller seller = new Seller(1L, "123-45-67891", "name");

		given(sellerMapper.findById(1L)).willReturn(Optional.of(seller));

		// when
		sellerService.deleteSeller(sellerId);

		// then
		verify(sellerMapper).update(any(Seller.class));
	}

	@Test
	@DisplayName("판매자 목록을 반환한다.")
	void should_returnSellers() {

		// given
		Long sellerId = 1L;

		given(sellerMapper.findByMemberId(sellerId)).willReturn(List.of(new Seller()));

		// when
		sellerService.getSellers(sellerId);

		// then
		verify(sellerMapper).findByMemberId(sellerId);
	}

	@Test
	@DisplayName("판매자 조회를 성공하면 판매자를 반환한다.")
	void should_returnSeller_when_getSellerSuccess() {

		// given
		Long sellerId = 1L;

		given(sellerMapper.findById(sellerId)).willReturn(Optional.of(new Seller()));

		// when
		sellerService.getSeller(sellerId);

		// then
		verify(sellerMapper).findById(sellerId);
	}
}
