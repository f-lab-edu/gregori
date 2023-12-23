package com.gregori.seller.mapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gregori.common.CustomMybatisTest;
import com.gregori.common.exception.NotFoundException;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.seller.domain.Seller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@CustomMybatisTest
class SellerMapperTest {

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private SellerMapper sellerMapper;

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

		if (member != null) {
			memberMapper.deleteById(member.getId());
			member = null;
		}
	}

	@Test
	@DisplayName("DB에 새로운 셀러를 추가한다.")
	void should_insert_when_validSeller() {

		// given
		Seller seller = Seller.builder()
			.memberId(member.getId())
			.businessNumber("111-11-11111")
			.businessName("김일호 상점1")
			.build();

		// when
		sellerMapper.insert(seller);
		sellerIds.add(seller.getId());
		Seller result = sellerMapper.findById(seller.getId()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result.getId(), seller.getId());
		assertEquals(result.getBusinessNumber(), seller.getBusinessNumber());
	}

	@Test
	@DisplayName("DB의 상품을 수정한다.")
	void should_update_when_idMatch() {

		// given
		Seller seller = Seller.builder()
			.memberId(member.getId())
			.businessNumber("111-11-11111")
			.businessName("김일호 상점1")
			.build();

		sellerMapper.insert(seller);
		sellerIds.add(seller.getId());

		// when
		seller.updateSellerInfo("222-22-22222", "김이호 상점");
		sellerMapper.update(seller);
		Seller result = sellerMapper.findById(seller.getId()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result.getId(), seller.getId());
		assertEquals(result.getBusinessNumber(), seller.getBusinessNumber());
		assertEquals(result.getBusinessName(), seller.getBusinessName());
	}

	@Test
	@DisplayName("Id 목록과 일치하는 Seller 테이블의 셀러를 전부 삭제한다.")
	void should_delete_when_idMatch() {

		// given
		Seller seller = Seller.builder()
			.memberId(member.getId())
			.businessNumber("111-11-11111")
			.businessName("김일호 상점1")
			.build();

		sellerMapper.insert(seller);
		sellerIds.add(seller.getId());

		// when
		sellerMapper.deleteByIds(List.of(seller.getId()));
		Seller result = sellerMapper.findById(seller.getId()).orElse(null);

		// then
		assertNull(result);
	}

	@Test
	@DisplayName("DB에서 memberId가 일치하는 셀러를 조회한다.")
	void should_find_when_memberIdMatch() {

		// given
		Seller seller = Seller.builder()
			.memberId(member.getId())
			.businessNumber("123-45-67890")
			.businessName("김일호 상점1")
			.build();

		sellerMapper.insert(seller);
		sellerIds.add(seller.getId());

		// when
		List<Seller> result = sellerMapper.findByMemberId(seller.getMemberId());

		// then
		assertEquals(result.size(), 1);
	}

	@Test
	@DisplayName("DB에서 id가 일치하는 셀러를 조회한다.")
	void should_find_when_idMatch() {

		// given
		Seller seller = Seller.builder()
			.memberId(member.getId())
			.businessNumber("123-45-67890")
			.businessName("김일호 상점1")
			.build();

		sellerMapper.insert(seller);
		sellerIds.add(seller.getId());

		// when
		Seller result = sellerMapper.findById(seller.getId()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result.getId(), seller.getId());
		assertEquals(result.getBusinessNumber(), seller.getBusinessNumber());
		assertEquals(result.getBusinessName(), seller.getBusinessName());
	}
}
