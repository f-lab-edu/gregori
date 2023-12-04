package com.gregori.item.service;

import java.util.List;

import com.gregori.item.dto.ItemInsertDto;
import com.gregori.item.dto.ItemResponseDto;
import com.gregori.item.dto.ItemUpdateDto;

public interface ItemService {
	Long insertItem(ItemInsertDto itemInsertDto);
	Long updateItem(ItemUpdateDto itemUpdateDto);
	Long preSaleItem(Long itemId);
	Long onSaleItem(Long itemId);
	Long endOfSaleItem(Long itemId);
	ItemResponseDto findItemById(Long itemId);
	List<ItemResponseDto> findAllItemById(List<Long> itemIds);
}
