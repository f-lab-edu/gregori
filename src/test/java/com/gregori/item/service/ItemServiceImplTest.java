package com.gregori.item.service;

import static com.gregori.item.domain.Item.Status.ON_SALE;
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
import com.gregori.item.domain.Item;
import com.gregori.item.dto.ItemCreateDto;
import com.gregori.item.dto.ItemResponseDto;
import com.gregori.item.dto.ItemUpdateDto;
import com.gregori.item.mapper.ItemMapper;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.seller.domain.Seller;
import com.gregori.seller.mapper.SellerMapper;

@SpringBootTest
@ActiveProfiles("test")
class ItemServiceImplTest {
	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private SellerMapper sellerMapper;

	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ItemService itemService;

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
			.businessNo("111-11-11111")
			.businessName("일호 상점")
			.build();
		sellerMapper.insert(seller);
	}

	@AfterEach
	void afterEach() {
		if (!itemIds.isEmpty()) {
			itemMapper.deleteByIds(itemIds);
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
		ItemCreateDto itemCreateDto = new ItemCreateDto(seller.getId(), "아이템1", 100L, 1L);

		// when
		Long result = itemService.saveItem(itemCreateDto);
		Item item = itemMapper.findById(result).orElseThrow(NotFoundException::new);
		itemIds.add(item.getId());

		// then
		assertEquals(result, item.getId());
		assertEquals(item.getName(), "아이템1");
		assertEquals(item.getPrice(), 100L);
		assertEquals(item.getInventory(), 1L);
	}

	@Test
	@DisplayName("DB에 저장된 상품을 수정하고 id를 반환한다.")
	void updateItem() {
		// given
		Item item = Item.builder()
			.sellerId(seller.getId())
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();
		itemMapper.insert(item);
		itemIds.add(item.getId());

		ItemUpdateDto itemUpdateDto = new ItemUpdateDto(item.getId(), "아이템 수정", 999L, 9L);

		// when
		Long result = itemService.updateItem(itemUpdateDto);
		Item findItem = itemMapper.findById(result).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result, findItem.getId());
		assertEquals(itemUpdateDto.getName(), "아이템 수정");
		assertEquals(itemUpdateDto.getPrice(), 999L);
		assertEquals(itemUpdateDto.getInventory(), 9L);
	}

	@Test
	@DisplayName("DB에 저장된 상품의 상태를 변경하고 id를 반환한다.")
	void updateItemStatus() {
		// given
		Item item = Item.builder()
			.sellerId(seller.getId())
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();
		itemMapper.insert(item);
		itemIds.add(item.getId());

		// when
		Long result = itemService.updateItemStatus(ON_SALE, item.getId());
		Item findItem = itemMapper.findById(result).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result, findItem.getId());
		assertEquals(findItem.getStatus(), ON_SALE);
	}

	@Test
	@DisplayName("상품의 id로 DB에 저장된 상품을 조회해서 반환한다.")
	void getItem() {
		// given
		Item item = Item.builder()
			.sellerId(seller.getId())
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();
		itemMapper.insert(item);
		itemIds.add(item.getId());

		// when
		ItemResponseDto result = itemService.getItem(item.getId());

		// then
		assertEquals(result.getId(), item.getId());
		assertEquals(result.getName(), item.getName());
		assertEquals(result.getPrice(), item.getPrice());
		assertEquals(result.getInventory(), item.getInventory());
		assertEquals(result.getStatus(), item.getStatus());
	}

	@Test
	@DisplayName("상품 id 목록으로 DB에 저장된 상품을 전부 조회해서 반환한다.")
	void getItems() {
		// given
		Item item1 = Item.builder()
			.sellerId(seller.getId())
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();
		Item item2 = Item.builder()
			.sellerId(seller.getId())
			.name("아이템2")
			.price(200L)
			.inventory(2L)
			.build();

		itemMapper.insert(item1);
		itemMapper.insert(item2);
		itemIds.add(item1.getId());
		itemIds.add(item2.getId());

		// when
		List<ItemResponseDto> result = itemService
			.getItems(List.of(item1.getId(), item2.getId()));

		// then
		assertEquals(result.get(0).getId(), item1.getId());
		assertEquals(result.get(0).getName(), item1.getName());
		assertEquals(result.get(1).getId(), item2.getId());
		assertEquals(result.get(1).getName(), item2.getName());
	}
}
