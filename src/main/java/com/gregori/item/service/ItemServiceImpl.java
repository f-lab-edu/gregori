package com.gregori.item.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.common.exception.NotFoundException;
import com.gregori.item.domain.Item;
import com.gregori.item.dto.ItemCreateDto;
import com.gregori.item.dto.ItemResponseDto;
import com.gregori.item.dto.ItemUpdateDto;
import com.gregori.item.mapper.ItemMapper;

import lombok.RequiredArgsConstructor;

import static com.gregori.item.domain.Item.Status.END_OF_SALE;
import static com.gregori.item.domain.Item.Status.ON_SALE;
import static com.gregori.item.domain.Item.Status.PRE_SALE;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
	private final ItemMapper itemMapper;

	@Override
	public Long saveItem(ItemCreateDto itemCreateDto) {
		Item item = itemCreateDto.toEntity();
		itemMapper.insert(item);

		return item.getId();
	}

	@Override
	@Transactional
	public Long updateItem(ItemUpdateDto itemUpdateDto) throws NotFoundException {
		Item item = itemMapper.findById(itemUpdateDto.getId()).orElseThrow(NotFoundException::new);
		item.updateItemInfo(itemUpdateDto.getName(), itemUpdateDto.getPrice(),
			itemUpdateDto.getInventory());
		itemMapper.update(item);

		return item.getId();
	}

	@Override
	@Transactional
	public Long updateItemStatus(Item.Status status, Long itemId) throws NotFoundException {
		Item item = itemMapper.findById(itemId).orElseThrow(NotFoundException::new);

		if (status == PRE_SALE) {
			item.preSale();
		} else if (status == ON_SALE) {
			item.onSale();
		} else if (status == END_OF_SALE) {
			item.endOfSale();
		}

		itemMapper.update(item);

		return item.getId();
	}

	@Override
	public ItemResponseDto getItem(Long itemId) {
		Item item = itemMapper.findById(itemId).orElseThrow(NotFoundException::new);

		return new ItemResponseDto().toEntity(item);
	}

	@Override
	public List<ItemResponseDto> getItems(List<Long> itemIds) {
		var itemList = itemMapper.findByIds(itemIds);

		return itemList.stream()
			.map(item -> new ItemResponseDto().toEntity(item))
			.toList();
	}
}
