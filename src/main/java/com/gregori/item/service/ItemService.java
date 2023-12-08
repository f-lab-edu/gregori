package com.gregori.item.service;

import java.util.List;

import com.gregori.common.exception.NotFoundException;
import com.gregori.item.domain.Item;
import com.gregori.item.dto.ItemCreateDto;
import com.gregori.item.dto.ItemResponseDto;
import com.gregori.item.dto.ItemUpdateDto;

public interface ItemService {
	Long saveItem(ItemCreateDto itemCreateDto);
	Long updateItem(ItemUpdateDto itemUpdateDto) throws NotFoundException;
	Long updateItemStatus(Item.Status status, Long itemId) throws NotFoundException;
	ItemResponseDto getItem(Long itemId) throws NotFoundException;
	List<ItemResponseDto> getItems(List<Long> itemIds);
}
