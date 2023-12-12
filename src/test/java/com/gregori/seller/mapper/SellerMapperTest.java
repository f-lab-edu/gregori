package com.gregori.seller.mapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.gregori.common.exception.NotFoundException;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.seller.domain.Seller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
class SellerMapperTest {
	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private SellerMapper sellerMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	Member member;
	List<Long> sellerIds = new ArrayList<>();

	@BeforeEach
	void beforeEach() {
		member = Member.builder()
			.email("a@a.a")
			.name("일호")
			.password(passwordEncoder.encode("aa11111!"))
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
			memberMapper.deleteByEmails(List.of(member.getEmail()));
			member = null;
		}
	}

	@Test
	@DisplayName("Sellers 테이블에 새로운 셀러를 추가한다.")
	void insert() {
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
	@DisplayName("Sellers 테이블의 상품을 수정한다.")
	void update() {
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
	void deleteByIds() {
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
	@DisplayName("Sellers 테이블에서 memberId가 일치하는 셀러를 조회한다.")
	void findByMemberId() {
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
	@DisplayName("Sellers 테이블에서 id가 일치하는 셀러를 조회한다.")
	void findById() {
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
