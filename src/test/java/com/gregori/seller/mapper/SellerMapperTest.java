package com.gregori.seller.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import static org.assertj.core.api.Assertions.assertThat;

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
	@DisplayName("새로운 판매자를 추가한다.")
	void should_insert() {

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
		assertThat(result.getId()).isEqualTo(seller.getId());
		assertThat(result.getBusinessNumber()).isEqualTo(seller.getBusinessNumber());
	}

	@Test
	@DisplayName("판매자를 수정한다.")
	void should_update() {

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
		assertThat(result.getId()).isEqualTo(seller.getId());
		assertThat(result.getBusinessNumber()).isEqualTo(seller.getBusinessNumber());
		assertThat(result.getBusinessName()).isEqualTo(seller.getBusinessName());
	}

	@Test
	@DisplayName("id 목록으로 판매자를 삭제한다.")
	void should_deleteByIds() {

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
		assertThat(result).isNull();
	}

	@Test
	@DisplayName("id로 판매자를 조회한다.")
	void should_findById() {

		// given
		Seller seller = Seller.builder()
			.memberId(member.getId())
			.businessNumber("123-45-67890")
			.businessName("김일호 상점1")
			.build();

		sellerMapper.insert(seller);
		sellerIds.add(seller.getId());

		// when
		Optional<Seller> result = sellerMapper.findById(seller.getId());

		// then
		assertThat(result.isPresent()).isTrue();
	}

	@Test
	@DisplayName("memberId로 판매자를 조회한다.")
	void should_findByMemberId() {

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
		assertThat(result.getId()).isEqualTo(seller.getId());
		assertThat(result.getBusinessNumber()).isEqualTo(seller.getBusinessNumber());
		assertThat(result.getBusinessName()).isEqualTo(seller.getBusinessName());
	}
}
