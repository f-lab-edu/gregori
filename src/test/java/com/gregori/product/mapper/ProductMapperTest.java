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

import static org.assertj.core.api.Assertions.assertThat;

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
	@DisplayName("새로운 상품을 추가한다.")
	void should_insert() {

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
		assertThat(result.getId()).isEqualTo(product.getId());
		assertThat(result.getSellerId()).isEqualTo(product.getSellerId());
		assertThat(result.getName()).isEqualTo(product.getName());
	}

	@Test
	@DisplayName("상품을 수정한다.")
	void should_update() {

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
		assertThat(result.getId()).isEqualTo(product.getId());
		assertThat(result.getSellerId()).isEqualTo(product.getSellerId());
		assertThat(result.getName()).isEqualTo("아이템 수정");
		assertThat(result.getPrice()).isEqualTo(999L);
		assertThat(result.getInventory()).isEqualTo(9L);
	}

	@Test
	@DisplayName("id 목록으로 상품을 삭제한다.")
	void should_deleteByIds() {

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
		assertThat(result).isNull();
	}

	@Test
	@DisplayName("id로 상품을 조회한다.")
	void should_findById() {

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
		assertThat(result.getId()).isEqualTo(product.getId());
		assertThat(result.getSellerId()).isEqualTo(product.getSellerId());
		assertThat(result.getName()).isEqualTo(product.getName());
		assertThat(result.getPrice()).isEqualTo(product.getPrice());
		assertThat(result.getInventory()).isEqualTo(product.getInventory());
		assertThat(result.getStatus()).isEqualTo(product.getStatus());
		assertThat(result.getCreatedAt()).isEqualTo(product.getCreatedAt());
		assertThat(result.getUpdatedAt()).isEqualTo(product.getUpdatedAt());
	}

	@Test
	@DisplayName("id 목록으로 상품을 조회한다.")
	void should_findByIds() {

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
		assertThat(result.get(0).getId()).isEqualTo(product1.getId());
		assertThat(result.get(0).getName()).isEqualTo(product1.getName());
		assertThat(result.get(1).getId()).isEqualTo(product2.getId());
		assertThat(result.get(1).getName()).isEqualTo(product2.getName());
	}

	@Test
	@DisplayName("sellerId로 상품을 조회한다.")
	void should_findBySellerId() {

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
		assertThat(result.getId()).isEqualTo(product.getId());
		assertThat(result.getSellerId()).isEqualTo(product.getSellerId());
		assertThat(result.getName()).isEqualTo(product.getName());
		assertThat(result.getPrice()).isEqualTo(product.getPrice());
		assertThat(result.getInventory()).isEqualTo(product.getInventory());
		assertThat(result.getStatus()).isEqualTo(product.getStatus());
		assertThat(result.getCreatedAt()).isEqualTo(product.getCreatedAt());
		assertThat(result.getUpdatedAt()).isEqualTo(product.getUpdatedAt());
	}
}
