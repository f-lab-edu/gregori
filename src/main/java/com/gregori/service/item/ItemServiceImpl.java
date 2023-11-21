package com.gregori.service.item;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.domain.item.Item;
import com.gregori.dto.item.ItemInsertDto;
import com.gregori.dto.item.ItemResponseDto;
import com.gregori.dto.item.ItemUpdateDto;
import com.gregori.mapper.ItemMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
	private final ItemMapper itemMapper;

	@Override
	@Transactional
	public Long insertItem(ItemInsertDto itemInsertDto) {
		return itemMapper.insert(Item.builder()
			.name(itemInsertDto.getName())
			.price(itemInsertDto.getPrice())
			.inventory(itemInsertDto.getInventory())
			.build());
	}

	@Override
	@Transactional
	public Long updateItem(ItemUpdateDto itemUpdateDto) {
		Item item = itemMapper.findById(itemUpdateDto.getId())
			.orElseThrow(() -> new RuntimeException("Item entity not found by id"));
		item.updateItemInfo(itemUpdateDto.getName(), itemUpdateDto.getPrice(),
			itemUpdateDto.getInventory());

		return itemMapper.update(item);
	}

	@Override
	@Transactional
	public Long preSaleItem(Long itemId) {
		Item item = itemMapper.findById(itemId)
			.orElseThrow(() -> new RuntimeException("Item entity not found by id"));
		item.preSale();

		return itemMapper.update(item);
	}

	@Override
	@Transactional
	public Long onSaleItem(Long itemId) {
		Item item = itemMapper.findById(itemId)
			.orElseThrow(() -> new RuntimeException("Item entity not found by id"));
		item.onSale();

		return itemMapper.update(item);
	}

	@Override
	@Transactional
	public Long endOfSaleItem(Long itemId) {
		Item item = itemMapper.findById(itemId)
			.orElseThrow(() -> new RuntimeException("Item entity not found by id"));
		item.endOfSale();

		return itemMapper.update(item);
	}

	@Override
	public ItemResponseDto findItemById(Long itemId) {
		Item item = itemMapper.findById(itemId)
			.orElseThrow(() -> new RuntimeException("Item entity not found by id"));

		return ItemResponseDto.builder()
			.id(item.getId())
			.name(item.getName())
			.price(item.getPrice())
			.inventory(item.getInventory())
			.status(item.getStatus())
			.build();
	}

	@Override
	public List<ItemResponseDto> findAllItemById(List<Long> itemIds) {
		var itemList = itemMapper.findAllById(itemIds);

		return itemList.stream()
			.map(item -> ItemResponseDto.builder()
				.id(item.getId())
				.name(item.getName())
				.price(item.getPrice())
				.inventory(item.getInventory())
				.status(item.getStatus())
				.build()
			)
			.collect(Collectors.toList());
	}
}
