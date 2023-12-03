package com.gregori.item.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.gregori.common.exception.NotFoundException;
import com.gregori.item.domain.Item;

@SpringBootTest
@ActiveProfiles("test")
class ItemMapperTest {
	@Autowired
	private ItemMapper itemMapper;

	List<Long> itemIds = new ArrayList<>();

 	@AfterEach
	void afterEach() {
		if (!itemIds.isEmpty()) {
			itemMapper.deleteByIds(itemIds);
			itemIds.clear();
		}
	}

	@Test
	@DisplayName("Item 삽입 테스트")
	void insert() {
		//given
		Item item = Item.builder()
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();

		// when
		itemMapper.insert(item);
		itemIds.add(item.getId());
		Item result = itemMapper.findById(item.getId()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result.getId(), item.getId());
		assertEquals(result.getName(), item.getName());
	}

	@Test
	@DisplayName("Item 수정 테스트")
	void update() {
		// given
		Item item = Item.builder()
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();

		itemMapper.insert(item);
		itemIds.add(item.getId());

		// when
		item.updateItemInfo("아이템 수정", 999L, 9L);
		itemMapper.update(item);
		Item result = itemMapper.findById(item.getId()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result.getId(), item.getId());
		assertEquals(result.getName(), "아이템 수정");
		assertEquals(result.getPrice(), 999L);
		assertEquals(result.getInventory(), 9L);
	}

	@Test
	@DisplayName("Items 삭제 테스트")
	void deleteByIds() {
		 // given
		Item item = Item.builder()
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();

		itemMapper.insert(item);
		itemIds.add(item.getId());

		// when
		itemMapper.deleteByIds(List.of(item.getId()));
		Item result = itemMapper.findById(item.getId()).orElse(null);

		// then
		assertNull(result);
	}

	@Test
	@DisplayName("Item 찾기 테스트")
	void findById() {
		// given
		Item item = Item.builder()
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();

		itemMapper.insert(item);
		itemIds.add(item.getId());

		// when
		Item result = itemMapper.findById(item.getId()).orElseThrow(NotFoundException::new);

		// then
		assertEquals(result.getId(), item.getId());
		assertEquals(result.getName(), item.getName());
		assertEquals(result.getPrice(), item.getPrice());
		assertEquals(result.getInventory(), item.getInventory());
		assertEquals(result.getStatus(), item.getStatus());
		assertEquals(result.getCreatedAt(), item.getCreatedAt());
		assertEquals(result.getUpdatedAt(), item.getUpdatedAt());
	}

	@Test
	@DisplayName("Items 찾기 테스트")
	void findAllById() {
		// given
		Item item1 = Item.builder()
			.name("아이템1")
			.price(100L)
			.inventory(1L)
			.build();
		Item item2 = Item.builder()
			.name("아이템2")
			.price(200L)
			.inventory(2L)
			.build();

		itemMapper.insert(item1);
		itemMapper.insert(item2);
		itemIds.add(item1.getId());
		itemIds.add(item2.getId());

		// when
		List<Item> result = itemMapper.findAllById(itemIds);

		// then
		assertEquals(result.get(0).getId(), item1.getId());
		assertEquals(result.get(0).getName(), item1.getName());
		assertEquals(result.get(1).getId(), item2.getId());
		assertEquals(result.get(1).getName(), item2.getName());
	}
}
