package com.gregori.product.mapper;

import java.util.List;
import java.util.Optional;
import java.util.Random;
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

import lombok.extern.slf4j.Slf4j;

import static com.gregori.common.domain.IsDeleted.TRUE;
import static com.gregori.product.domain.Product.Status.ON_SALE;
import static com.gregori.product.domain.Sorter.CREATED_AT_DESC;
import static com.gregori.product.domain.Sorter.PRICE_ASC;
import static com.gregori.product.domain.Sorter.PRICE_DESC;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
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
	List<Long> categoryIds = new CopyOnWriteArrayList<>();
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

		Category category1 = new Category("모자");
		Category category2 = new Category("아우터");
		categoryMapper.insert(category1);
		categoryMapper.insert(category2);
		categoryIds.add(category1.getId());
		categoryIds.add(category2.getId());
	}

	@AfterEach
	void afterEach() {
		if (!productIds.isEmpty()) {
			productIds.forEach(id -> productMapper.deleteById(id));
			productIds.clear();
		}
		if (!categoryIds.isEmpty()) {
			categoryIds.forEach(id -> categoryMapper.deleteById(id));
			categoryIds.clear();
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
			.categoryId(categoryIds.get(0))
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
			.categoryId(categoryIds.get(0))
			.name("name")
			.price(1L)
			.inventory(1L)
			.build();

		productMapper.insert(product);
		productIds.add(product.getId());

		// when
		product.updateProductInfo(categoryIds.get(0), "newName", 2L, 2L, ON_SALE);
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
	@DisplayName("상품 재고를 수정한다.")
	void should_updateInventory() {

		// given
		Product product = Product.builder()
			.sellerId(seller.getId())
			.categoryId(categoryIds.get(0))
			.name("name")
			.price(1L)
			.inventory(1L)
			.build();

		productMapper.insert(product);
		productIds.add(product.getId());

		// when
		productMapper.updateInventory(product.getId(), 10L);
		Optional<Product> result = productMapper.findById(product.getId());

		// then
		assertThat(result.isPresent()).isTrue();
		assertThat(result.get().getInventory()).isEqualTo(10L);
	}

	@Test
	@DisplayName("id로 상품을 논리적으로 삭제한다.")
	void should_updateIsDeleted() {

		// given
		Product product = Product.builder()
			.sellerId(seller.getId())
			.categoryId(categoryIds.get(0))
			.name("name")
			.price(1L)
			.inventory(1L)
			.build();

		productMapper.insert(product);
		productIds.add(product.getId());

		// when
		productMapper.updateIsDeleted(product.getId(), TRUE);
		Optional<Product> result = productMapper.findById(product.getId());

		// then
		assertThat(result.isPresent()).isFalse();
	}

	@Test
	@DisplayName("id로 상품을 삭제한다.")
	void should_deleteById() {

		 // given
		Product product = Product.builder()
			.sellerId(seller.getId())
			.categoryId(categoryIds.get(0))
			.name("name")
			.price(1L)
			.inventory(1L)
			.build();

		productMapper.insert(product);
		productIds.add(product.getId());

		// when
		productMapper.deleteById(product.getId());
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
			.categoryId(categoryIds.get(0))
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
	@DisplayName("낮은 가격순으로 keyword와 일치하는 상품을 조회한다.")
	void should_findByKeyword_when_priceAsc() {

		// given
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			String name = i % 2 == 0 ? "one" : "two";
			Long price = random.nextLong(0, 999);
			Product product = new Product(seller.getId(), categoryIds.get(0), name+i, price, (long)i);
			productMapper.insert(product);
			productIds.add(product.getId());
		}

		// when
		List<Product> result = productMapper.find("one", null, null, 5, 0, PRICE_ASC.toString());

		// then
		assertThat(result.size()).isEqualTo(5);
		for (int i = 0; i < result.size() - 1; i++) {
			assertThat(result.get(i).getName()).contains("one");
			assertThat(result.get(i).getPrice() < result.get(i + 1).getPrice()).isTrue();
		}
	}

	@Test
	@DisplayName("높은 가격순으로 categoryId와 일치하는 상품을 조회한다.")
	void should_findByCategory_when_priceDesc() {

		// given
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			Long categoryId = i % 2 == 0 ? categoryIds.get(0) : categoryIds.get(1);
			Long price = random.nextLong(0, 999);
			Product product = new Product(seller.getId(), categoryId, "name"+i, price, (long)i);
			productMapper.insert(product);
			productIds.add(product.getId());
		}

		// when
		List<Product> result = productMapper.find(null, categoryIds.get(1), null,5, 0, PRICE_DESC.toString());

		// then
		assertThat(result.size()).isEqualTo(5);
		for (int i = 0; i < result.size() - 1; i++) {
			assertThat(result.get(i).getCategoryId()).isEqualTo(categoryIds.get(1));
			assertThat(result.get(i).getPrice() > result.get(i + 1).getPrice()).isTrue();
		}
	}

	@Test
	@DisplayName("최신 등록 순으로 sellerId와 일치하는 상품을 조회한다.")
	void should_findBySellerId_when_createdAtDesc() {

		// given
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			Long price = random.nextLong(0, 999);
			Product product = new Product(seller.getId(), categoryIds.get(0), "name"+i, price, (long)i);
			productMapper.insert(product);
			productIds.add(product.getId());
		}

		// when
		List<Product> result = productMapper.find(null, null, seller.getId(), 5, 0, CREATED_AT_DESC.toString());

		// then
		assertThat(result.size()).isEqualTo(5);
		for (int i = 0; i < result.size() - 1; i++) {
			assertThat(result.get(i).getSellerId()).isEqualTo(seller.getId());
		}
	}
}
