package com.gregori.product.mapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gregori.common.CustomMybatisTest;
import com.gregori.common.exception.NotFoundException;
import com.gregori.product.domain.Product;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.seller.domain.Seller;
import com.gregori.seller.mapper.SellerMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@CustomMybatisTest
class ProductMapperTest {

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private SellerMapper sellerMapper;

	@Autowired
	private ProductMapper productMapper;

	Member member;
	Seller seller;
	List<Long> itemIds = new ArrayList<>();

	@BeforeEach
	void beforeEach() {
		member = Member.builder()
			.email("a@a.a")
			.name("일호")
			.password("aa11111!")
			.build();
		memberMapper.insert(member);

		seller = Seller.builder()
			.memberId(member.getId())
			.businessNumber("111-11-11111")
			.businessName("일호 상점")
			.build();
		sellerMapper.insert(seller);
	}

	@AfterEach
	void afterEach() {
		if (!itemIds.isEmpty()) {
			productMapper.deleteByIds(itemIds);
			itemIds.clear();
		}
		if (seller != null) {
			sellerMapper.deleteByIds(List.of(seller.getId()));
			seller = null;
		}
	}

	@Test
	@DisplayName("DB에 새로운 상품을 추가한다.")
	void should_insert_when_validProduct() {

		// given
		Product product = Product.builder()
			.sellerId(seller.getId())
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();

		// when
		productMapper.insert(product);
		itemIds.add(product.getId());
		Product result = productMapper.findById(product.getId()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result.getId(), product.getId());
		assertEquals(result.getSellerId(), product.getSellerId());
		assertEquals(result.getName(), product.getName());
	}

	@Test
	@DisplayName("DB의 상품을 수정한다.")
	void should_update_when_idMatch() {

		// given
		Product product = Product.builder()
			.sellerId(seller.getId())
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();

		productMapper.insert(product);
		itemIds.add(product.getId());

		// when
		product.updateProductInfo("아이템 수정", 999L, 9L);
		productMapper.update(product);
		Product result = productMapper.findById(product.getId()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result.getId(), product.getId());
		assertEquals(result.getSellerId(), product.getSellerId());
		assertEquals(result.getName(), "아이템 수정");
		assertEquals(result.getPrice(), 999L);
		assertEquals(result.getInventory(), 9L);
	}

	@Test
	@DisplayName("Id 목록과 일치하는 DB의 상품을 전부 삭제한다.")
	void should_delete_when_IdMatch() {

		 // given
		Product product = Product.builder()
			.sellerId(seller.getId())
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();

		productMapper.insert(product);
		itemIds.add(product.getId());

		// when
		productMapper.deleteByIds(List.of(product.getId()));
		Product result = productMapper.findById(product.getId()).orElse(null);

		// then
		assertNull(result);
	}

	@Test
	@DisplayName("DB에서 id가 일치하는 상품을 조회한다.")
	void should_find_when_idMatch() {

		// given
		Product product = Product.builder()
			.sellerId(seller.getId())
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();

		productMapper.insert(product);
		itemIds.add(product.getId());

		// when
		Product result = productMapper.findById(product.getId()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result.getId(), product.getId());
		assertEquals(result.getSellerId(), product.getSellerId());
		assertEquals(result.getName(), product.getName());
		assertEquals(result.getPrice(), product.getPrice());
		assertEquals(result.getInventory(), product.getInventory());
		assertEquals(result.getStatus(), product.getStatus());
		assertEquals(result.getCreatedAt(), product.getCreatedAt());
		assertEquals(result.getUpdatedAt(), product.getUpdatedAt());
	}

	@Test
	@DisplayName("DB에서 id 목록과 일치하는 상품을 전부 조회한다.")
	void should_find_when_ids_match() {

		// given
		Product product1 = Product.builder()
			.sellerId(seller.getId())
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();
		Product product2 = Product.builder()
			.sellerId(seller.getId())
			.name("아이템2")
			.price(200L)
			.inventory(2L)
			.build();

		productMapper.insert(product1);
		productMapper.insert(product2);
		itemIds.add(product1.getId());
		itemIds.add(product2.getId());

		// when
		List<Product> result = productMapper.findByIds(itemIds);

		// then
		assertEquals(result.get(0).getId(), product1.getId());
		assertEquals(result.get(0).getName(), product1.getName());
		assertEquals(result.get(1).getId(), product2.getId());
		assertEquals(result.get(1).getName(), product2.getName());
	}

	@Test
	@DisplayName("DB에서 sellerId가 일치하는 상품 목록을 조회한다.")
	void shoud_find_when_sellerIdMatch() {

		// given
		Product product = Product.builder()
			.sellerId(seller.getId())
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();

		productMapper.insert(product);
		itemIds.add(product.getId());

		// when
		Product result = productMapper.findById(product.getId()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result.getId(), product.getId());
		assertEquals(result.getSellerId(), product.getSellerId());
		assertEquals(result.getName(), product.getName());
		assertEquals(result.getPrice(), product.getPrice());
		assertEquals(result.getInventory(), product.getInventory());
		assertEquals(result.getStatus(), product.getStatus());
		assertEquals(result.getCreatedAt(), product.getCreatedAt());
		assertEquals(result.getUpdatedAt(), product.getUpdatedAt());
	}

}
