package com.gregori.product.service;

import static com.gregori.product.domain.Product.Status.ON_SALE;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import com.gregori.product.domain.Product;
import com.gregori.product.dto.ProductCreateDto;
import com.gregori.product.dto.ProductResponseDto;
import com.gregori.product.dto.ProductUpdateDto;
import com.gregori.product.mapper.ProductMapper;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.seller.domain.Seller;
import com.gregori.seller.mapper.SellerMapper;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceImplTest {

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private SellerMapper sellerMapper;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ProductService productService;

	Member member;
	Seller seller;
	List<Long> itemIds = new ArrayList<>();

	@BeforeEach
	void beforeEach() {
		member = Member.builder()
			.email("a@a.a")
			.name("일호")
			.password(passwordEncoder.encode("aa11111!"))
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
	@DisplayName("새로운 상품을 저장하고 id를 반환한다.")
	void saveItem() {

		// given
		ProductCreateDto productCreateDto = new ProductCreateDto(seller.getId(), "아이템1", 100L, 1L);

		// when
		Long result = productService.saveProduct(productCreateDto);
		Product product = productMapper.findById(result).orElseThrow(NotFoundException::new);
		itemIds.add(product.getId());

		// then
		assertEquals(result, product.getId());
		assertEquals(product.getName(), "아이템1");
		assertEquals(product.getPrice(), 100L);
		assertEquals(product.getInventory(), 1L);
	}

	@Test
	@DisplayName("DB에 저장된 상품을 수정하고 id를 반환한다.")
	void updateItem() {

		// given
		Product product = Product.builder()
			.sellerId(seller.getId())
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();
		productMapper.insert(product);
		itemIds.add(product.getId());

		ProductUpdateDto productUpdateDto = new ProductUpdateDto(product.getId(), "아이템 수정", 999L, 9L);

		// when
		Long result = productService.updateProduct(productUpdateDto);
		Product findProduct = productMapper.findById(result).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result, findProduct.getId());
		assertEquals(productUpdateDto.getName(), "아이템 수정");
		assertEquals(productUpdateDto.getPrice(), 999L);
		assertEquals(productUpdateDto.getInventory(), 9L);
	}

	@Test
	@DisplayName("DB에 저장된 상품의 상태를 변경하고 id를 반환한다.")
	void updateItemStatus() {

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
		Long result = productService.updateProductStatus(ON_SALE, product.getId());
		Product findProduct = productMapper.findById(result).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result, findProduct.getId());
		assertEquals(findProduct.getStatus(), ON_SALE);
	}

	@Test
	@DisplayName("상품의 id로 DB에 저장된 상품을 조회해서 반환한다.")
	void getItem() {

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
		ProductResponseDto result = productService.getProduct(product.getId());

		// then
		assertEquals(result.getId(), product.getId());
		assertEquals(result.getName(), product.getName());
		assertEquals(result.getPrice(), product.getPrice());
		assertEquals(result.getInventory(), product.getInventory());
		assertEquals(result.getStatus(), product.getStatus());
	}

	@Test
	@DisplayName("상품 id 목록으로 DB에 저장된 상품을 전부 조회해서 반환한다.")
	void getItems() {

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
		List<ProductResponseDto> result = productService
			.getProducts(List.of(product1.getId(), product2.getId()));

		// then
		assertEquals(result.get(0).getId(), product1.getId());
		assertEquals(result.get(0).getName(), product1.getName());
		assertEquals(result.get(1).getId(), product2.getId());
		assertEquals(result.get(1).getName(), product2.getName());
	}
}
