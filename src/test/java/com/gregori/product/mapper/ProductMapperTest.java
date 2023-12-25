package com.gregori.product.mapper;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gregori.category.domain.Category;
import com.gregori.category.mapper.CategoryMapper;
import com.gregori.common.CustomMybatisTest;
import com.gregori.product.domain.Product;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.seller.domain.Seller;
import com.gregori.seller.mapper.SellerMapper;

import static com.gregori.product.domain.Product.Status.ON_SALE;
import static org.assertj.core.api.Assertions.assertThat;

@CustomMybatisTest
class ProductMapperTest {

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private SellerMapper sellerMapper;

	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private ProductMapper productMapper;

	Member member;
	Seller seller;
	Category category;
	List<Long> productIds = new CopyOnWriteArrayList<>();

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

		category = new Category("name");
		categoryMapper.insert(category);
	}

	@AfterEach
	void afterEach() {
		if (!productIds.isEmpty()) {
			productMapper.deleteByIds(productIds);
			productIds.clear();
		}
		if (category != null) {
			categoryMapper.deleteById(category.getId());
			category = null;
		}
		if (seller != null) {
			sellerMapper.deleteByIds(List.of(seller.getId()));
			seller = null;
		}
		if (member != null) {
			memberMapper.deleteById(member.getId());
			member = null;
		}
	}

	@Test
	@DisplayName("새로운 상품을 추가한다.")
	void should_insert() {

		// given
		Product product = Product.builder()
			.sellerId(seller.getId())
			.categoryId(category.getId())
			.name("name")
			.price(1L)
			.inventory(1L)
			.build();

		// when
		productMapper.insert(product);
		productIds.add(product.getId());
		Optional<Product> result = productMapper.findById(product.getId());

		// then
		assertThat(product.getId()).isNotNull();
		assertThat(result.isPresent()).isTrue();
	}

	@Test
	@DisplayName("상품을 수정한다.")
	void should_update() {

		// given
		Product product = Product.builder()
			.sellerId(seller.getId())
			.categoryId(category.getId())
			.name("name")
			.price(1L)
			.inventory(1L)
			.build();

		productMapper.insert(product);
		productIds.add(product.getId());

		// when
		product.updateProductInfo(category.getId(), "newName", 2L, 2L, ON_SALE);
		productMapper.update(product);
		Optional<Product> result = productMapper.findById(product.getId());

		// then
		assertThat(result.isPresent()).isTrue();
		assertThat(result.get().getId()).isEqualTo(product.getId());
		assertThat(result.get().getSellerId()).isEqualTo(product.getSellerId());
		assertThat(result.get().getName()).isEqualTo("newName");
		assertThat(result.get().getPrice()).isEqualTo(2L);
		assertThat(result.get().getInventory()).isEqualTo(2L);
	}

	@Test
	@DisplayName("id 목록으로 상품을 삭제한다.")
	void should_deleteByIds() {

		 // given
		Product product = Product.builder()
			.sellerId(seller.getId())
			.categoryId(category.getId())
			.name("name")
			.price(1L)
			.inventory(1L)
			.build();

		productMapper.insert(product);
		productIds.add(product.getId());

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
			.categoryId(category.getId())
			.name("name")
			.price(1L)
			.inventory(1L)
			.build();

		productMapper.insert(product);
		productIds.add(product.getId());

		// when
		Optional<Product> result = productMapper.findById(product.getId());

		// then
		assertThat(result.isPresent()).isTrue();
		assertThat(result.get().getId()).isEqualTo(product.getId());
	}

	@Test
	@DisplayName("id 목록으로 상품을 조회한다.")
	void should_findByIds() {

		// given
		Product product1 = Product.builder()
			.sellerId(seller.getId())
			.categoryId(category.getId())
			.name("name")
			.price(1L)
			.inventory(1L)
			.build();
		Product product2 = Product.builder()
			.sellerId(seller.getId())
			.categoryId(category.getId())
			.name("name")
			.price(2L)
			.inventory(2L)
			.build();

		productMapper.insert(product1);
		productMapper.insert(product2);
		productIds.add(product1.getId());
		productIds.add(product2.getId());

		// when
		List<Product> result = productMapper.findByIds(productIds);

		// then
		assertThat(result.size()).isEqualTo(2);
		assertThat(result.get(0).getId()).isEqualTo(product1.getId());
		assertThat(result.get(1).getId()).isEqualTo(product2.getId());
	}

	@Test
	@DisplayName("sellerId로 상품을 조회한다.")
	void should_findBySellerId() {

		// given
		Product product = Product.builder()
			.sellerId(seller.getId())
			.categoryId(category.getId())
			.name("name")
			.price(1L)
			.inventory(1L)
			.build();

		productMapper.insert(product);
		productIds.add(product.getId());

		// when
		Optional<Product> result = productMapper.findById(product.getId());

		// then
		assertThat(result.isPresent()).isTrue();
		assertThat(result.get().getId()).isEqualTo(product.getId());
	}
}
