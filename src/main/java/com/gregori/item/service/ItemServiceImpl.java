package com.gregori.item.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.common.exception.NotFoundException;
import com.gregori.item.domain.Item;
import com.gregori.item.dto.ItemInsertDto;
import com.gregori.item.dto.ItemResponseDto;
import com.gregori.item.dto.ItemUpdateDto;
import com.gregori.item.mapper.ItemMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
	private final ItemMapper itemMapper;

	@Override
	@Transactional
	public Long insertItem(ItemInsertDto itemInsertDto) {
		Item item = itemInsertDto.toEntity();
		itemMapper.insert(item);

		return item.getId();
	}

	@Override
	@Transactional
	public Long updateItem(ItemUpdateDto itemUpdateDto) {
		Item item = itemMapper.findById(itemUpdateDto.getId())
			.orElseThrow(NotFoundException::new);
		item.updateItemInfo(itemUpdateDto.getName(), itemUpdateDto.getPrice(),
			itemUpdateDto.getInventory());
		itemMapper.update(item);

		return item.getId();
	}

	@Override
	@Transactional
	public Long preSaleItem(Long itemId) {
		Item item = itemMapper.findById(itemId)
			.orElseThrow(NotFoundException::new);
		item.preSale();
		itemMapper.update(item);

		return item.getId();
	}

	@Override
	@Transactional
	public Long onSaleItem(Long itemId) {
		Item item = itemMapper.findById(itemId)
			.orElseThrow(NotFoundException::new);
		item.onSale();
		itemMapper.update(item);

		return item.getId();
	}

	@Override
	@Transactional
	public Long endOfSaleItem(Long itemId) {
		Item item = itemMapper.findById(itemId)
			.orElseThrow(NotFoundException::new);
		item.endOfSale();
		itemMapper.update(item);

		return item.getId();
	}

	@Override
	public ItemResponseDto findItemById(Long itemId) {
		Item item = itemMapper.findById(itemId)
			.orElseThrow(NotFoundException::new);

		return new ItemResponseDto().toEntity(item);
	}

	@Override
	public List<ItemResponseDto> findAllItemById(List<Long> itemIds) {
		var itemList = itemMapper.findAllById(itemIds);

		return itemList.stream()
			.map(item -> new ItemResponseDto().toEntity(item))
			.collect(Collectors.toList());
	}
}
