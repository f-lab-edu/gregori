package com.gregori.seller.mapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.gregori.common.exception.NotFoundException;
import com.gregori.seller.domain.Seller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
class SellerMapperTest {
	@Autowired
	private SellerMapper sellerMapper;

	List<Long> sellerIds = new ArrayList<>();

	@AfterEach
	void afterEach() {
		if (!sellerIds.isEmpty()) {
			sellerMapper.deleteByIds(sellerIds);
			sellerIds.clear();
		}
	}

	@Test
	@DisplayName("Sellers 테이블에 새로운 셀러를 추가한다.")
	void insert() {
		// given
		Seller seller = Seller.builder()
			.name("김일호")
			.email("a@a.a")
			.password("aa11111!")
			.businessNo("123-45-67890")
			.businessName("김일호 상점1")
			.build();

		// when
		sellerMapper.insert(seller);
		sellerIds.add(seller.getId());
		Seller result = sellerMapper.findById(seller.getId()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result.getId(), seller.getId());
		assertEquals(result.getName(), seller.getName());
	}

	@Test
	@DisplayName("Sellers 테이블의 상품을 수정한다.")
	void update() {
		// given
		Seller seller = Seller.builder()
			.name("김일호")
			.email("a@a.a")
			.password("aa11111!")
			.businessNo("123-45-67890")
			.businessName("김일호 상점1")
			.build();

		sellerMapper.insert(seller);
		sellerIds.add(seller.getId());

		// when
		seller.updateSellerInfo("김이호", "bb22222@", "김이호 상점");
		sellerMapper.update(seller);
		Seller result = sellerMapper.findById(seller.getId()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result.getId(), seller.getId());
		assertEquals(result.getName(), seller.getName());
		assertEquals(result.getPassword(), seller.getPassword());
		assertEquals(result.getBusinessName(), seller.getBusinessName());
	}

	@Test
	@DisplayName("Id 목록과 일치하는 Seller 테이블의 셀러를 전부 삭제한다.")
	void deleteByIds() {
		// given
		Seller seller = Seller.builder()
			.name("김일호")
			.email("a@a.a")
			.password("aa11111!")
			.businessNo("123-45-67890")
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
	@DisplayName("Sellers 테이블에서 id가 일치하는 상품을 조회한다.")
	void findById() {
		// given
		Seller seller = Seller.builder()
			.name("김일호")
			.email("a@a.a")
			.password("aa11111!")
			.businessNo("123-45-67890")
			.businessName("김일호 상점1")
			.build();

		sellerMapper.insert(seller);
		sellerIds.add(seller.getId());

		// when
		Seller result = sellerMapper.findById(seller.getId()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result.getId(), seller.getId());
		assertEquals(result.getName(), seller.getName());
		assertEquals(result.getEmail(), seller.getEmail());
		assertEquals(result.getPassword(), seller.getPassword());
		assertEquals(result.getBusinessNo(), seller.getBusinessNo());
		assertEquals(result.getBusinessName(), seller.getBusinessName());
		assertEquals(result.getStatus(), seller.getStatus());
	}
}
