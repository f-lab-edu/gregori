package com.gregori.seller.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.gregori.common.exception.NotFoundException;
import com.gregori.common.exception.ValidationException;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.seller.domain.Seller;
import com.gregori.seller.dto.SellerRegisterDto;
import com.gregori.seller.dto.SellerResponseDto;
import com.gregori.seller.dto.SellerUpdateDto;
import com.gregori.seller.mapper.SellerMapper;

import static com.gregori.auth.domain.Authority.SELLING_MEMBER;
import static com.gregori.seller.domain.Seller.Status.CLOSED;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class SellerServiceImplTest {
	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private SellerMapper sellerMapper;

	@Autowired
	private SellerService sellerService;

	@Autowired
	private SellerServiceImpl sellerServiceImpl;

	Member member;
	List<Long> sellerIds = new ArrayList<>();

	@BeforeEach
	void beforeEach() {
		member = Member.builder()
			.email("a@a.a")
			.name("일호")
			.password("aa11111!")
			.build();
		memberMapper.insert(member);
	}

	@AfterEach
	void afterEach() {
		if (!sellerIds.isEmpty()) {
			sellerMapper.deleteByIds(sellerIds);
			sellerIds.clear();
		}
		if(member != null) {
			memberMapper.deleteByEmails(List.of(member.getEmail()));
			member = null;
		}
	}


	@Test
	@DisplayName("새로운 셀러를 DB에 저장하고 id를 반환한다.")
	void saveSeller() {
		// given
		SellerRegisterDto sellerRegisterDto = new SellerRegisterDto(member.getId(), "123-45-67891", "일호 상점");

		// when
		Long result = sellerService.saveSeller(sellerRegisterDto);
		Member findMember = memberMapper.findById(member.getId()).orElseThrow(NotFoundException::new);
		Seller seller = sellerMapper.findById(result).orElseThrow(NotFoundException::new);
		sellerIds.add(seller.getId());

		// then
		assertEquals(result, seller.getId());
		assertEquals(findMember.getAuthority(), SELLING_MEMBER);
		assertEquals(seller.getBusinessNumber(), "123-45-67891");
		assertEquals(seller.getBusinessName(), "일호 상점");
	}

	@Test
	@DisplayName("DB에 저장된 셀러를 수정하고 id를 반환한다.")
	void updateSeller() {
		// given
		Seller seller = Seller.builder()
			.memberId(member.getId())
			.businessNumber("111-11-11111")
			.businessName("일호 상점")
			.build();
		sellerMapper.insert(seller);
		sellerIds.add(seller.getId());

		SellerUpdateDto sellerUpdateDto = new SellerUpdateDto(seller.getId(), seller.getMemberId(), "123-45-67891", "이호 상점");

		// when
		Long result = sellerService.updateSeller(sellerUpdateDto);
		Seller findSeller = sellerMapper.findById(result).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result, findSeller.getId());
		assertEquals(sellerUpdateDto.getBusinessNumber(), findSeller.getBusinessNumber());
		assertEquals(sellerUpdateDto.getBusinessName(), findSeller.getBusinessName());
	}

	@Test
	@DisplayName("DB에 저장된 셀러의 상태를 변경하고 id를 반환한다.")
	void deleteSeller() {
		// given
		Seller seller = Seller.builder()
			.memberId(member.getId())
			.businessNumber("111-11-11111")
			.businessName("일호 상점")
			.build();
		sellerMapper.insert(seller);
		sellerIds.add(seller.getId());

		// when
		Long result = sellerService.deleteSeller(seller.getId());
		Seller findSeller = sellerMapper.findById(seller.getId()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result, findSeller.getId());
		assertEquals(findSeller.getStatus(), CLOSED);

	}

	@Test
	@DisplayName("회원 id로 DB에 저장된 회원의 셀러 정보를 전부 조회해서 반환한다.")
	void getSellers() {
		// given
		Seller seller1 = Seller.builder()
			.memberId(member.getId())
			.businessNumber("111-11-11111")
			.businessName("일호 상점")
			.build();
		Seller seller2 = Seller.builder()
			.memberId(member.getId())
			.businessNumber("222-22-22222")
			.businessName("이호 상점")
			.build();

		sellerMapper.insert(seller1);
		sellerMapper.insert(seller2);
		sellerIds.add(seller1.getId());
		sellerIds.add(seller2.getId());

		// when
		List<SellerResponseDto> result = sellerService.getSellers(member.getId());

		// then
		assertEquals(result.size(), 2);
		assertEquals(result.get(0).getId(), seller1.getId());
		assertEquals(result.get(1).getId(), seller2.getId());
	}

	@Test
	@DisplayName("셀러 id로 DB에 저장된 셀러를 조회해서 반환한다.")
	void getSeller() {
		// given
		Seller seller = Seller.builder()
			.memberId(member.getId())
			.businessNumber("111-11-11111")
			.businessName("일호 상점")
			.build();
		sellerMapper.insert(seller);
		sellerIds.add(seller.getId());

		// when
		SellerResponseDto result = sellerService.getSeller(seller.getId());

		// then
		assertEquals(result.getId(), seller.getId());
		assertEquals(result.getMemberId(), seller.getMemberId());
		assertEquals(result.getBusinessNumber(), seller.getBusinessNumber());
	}

	@Test
	@DisplayName("사업자 등록번호의 유효성을 검증하고 유효하지 않으면 false를 반환한다.")
	void invalidBusinessNumberInputFailsTest() {
		// given
		String businessNumber1 = "111-11-111";
		String businessNumber2 = "111-11-111111";

		String businessNumber3 = "000-45-67891";
		String businessNumber4 = "123-00-67891";
		String businessNumber5 = "123-45-00000";
		String businessNumber6 = "123-45-99999";

		// when
		Throwable result1 = catchThrowable(() -> sellerServiceImpl.businessNumberValidationCheck(businessNumber1));
		Throwable result2 = catchThrowable(() -> sellerServiceImpl.businessNumberValidationCheck(businessNumber2));
		Boolean result3 = sellerServiceImpl.businessNumberValidationCheck(businessNumber3);
		Boolean result4 = sellerServiceImpl.businessNumberValidationCheck(businessNumber4);
		Boolean result5 = sellerServiceImpl.businessNumberValidationCheck(businessNumber5);
		Boolean result6 = sellerServiceImpl.businessNumberValidationCheck(businessNumber6);

		// then
		then(result1).isInstanceOf(ValidationException.class).hasMessageContaining("유효한 값이 아닙니다.");
		then(result2).isInstanceOf(ValidationException.class).hasMessageContaining("유효한 값이 아닙니다.");
		assertEquals(result3, false);
		assertEquals(result4, false);
		assertEquals(result5, false);
		assertEquals(result6, false);
	}

	@Test
	@DisplayName("사업자 등록번호의 유효성을 검증하고 유효하면 true를 반환한다.")
	void validBusinessNumberInputSucceedsTest() {
		// given
		String businessNumber = "123-45-67891";

		// when
		Boolean result = sellerServiceImpl.businessNumberValidationCheck(businessNumber);

		// then
		assertEquals(result, true);
	}
}
