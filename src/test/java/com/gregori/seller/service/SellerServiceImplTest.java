package com.gregori.seller.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gregori.common.exception.ValidationException;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.product.mapper.ProductMapper;
import com.gregori.seller.domain.Seller;
import com.gregori.seller.dto.SellerRegisterDto;
import com.gregori.seller.dto.SellerUpdateDto;
import com.gregori.seller.mapper.SellerMapper;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
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
	@DisplayName("새로운 셀러를 DB에 저장하고 id를 반환한다.")
	void should_saveSeller() {

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
	@DisplayName("잘못된 사업자 등록번호면 셀러 생성을 실패한다.")
	void should_createMemberFail_when_invalidBusinessNumberInput() {

		// given
		SellerRegisterDto dto = new SellerRegisterDto(1L, "111-11-11111", "businessName");

		// when
		Throwable result = catchThrowable(() -> sellerService.saveSeller(dto));

		// then
		then(result).isInstanceOf(ValidationException.class).hasMessageContaining("유효하지 않은");
	}

	@Test
	@DisplayName("DB에 저장된 셀러를 수정하고 id를 반환한다.")
	void should_updateSeller() {

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
	@DisplayName("잘못된 사업자 등록번호면 업데이트를 실패한다.")
	void should_updateMemberFail_when_invalidBusinessNumberInput() {

		SellerUpdateDto sellerUpdateDto = new SellerUpdateDto(1L, 1L, "111-11-11111", "name");

		// when
		Throwable result = catchThrowable(() -> sellerService.updateSeller(sellerUpdateDto));

		// then
		then(result).isInstanceOf(ValidationException.class).hasMessageContaining("유효하지 않은");
	}

	@Test
	@DisplayName("DB에 저장된 셀러의 상태를 변경하고 id를 반환한다.")
	void should_deleteSeller() {

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
	@DisplayName("회원 id로 DB에 저장된 회원의 셀러 정보를 전부 조회해서 반환한다.")
	void should_getSellers() {

		// given
		Long sellerId = 1L;

		given(sellerMapper.findByMemberId(sellerId)).willReturn(List.of(new Seller()));

		// when
		sellerService.getSellers(sellerId);

		// then
		verify(sellerMapper).findByMemberId(sellerId);
	}

	@Test
	@DisplayName("셀러 id로 DB에 저장된 셀러를 조회해서 반환한다.")
	void should_getSeller() {

		// given
		Long sellerId = 1L;

		given(sellerMapper.findById(sellerId)).willReturn(Optional.of(new Seller()));

		// when
		sellerService.getSeller(sellerId);

		// then
		verify(sellerMapper).findById(sellerId);
	}
}
