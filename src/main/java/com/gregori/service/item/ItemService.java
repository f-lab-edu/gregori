package com.gregori.service.item;

import java.util.List;

import com.gregori.dto.item.ItemInsertDto;
import com.gregori.dto.item.ItemResponseDto;
import com.gregori.dto.item.ItemUpdateDto;

public interface ItemService {
	Long insertItem(ItemInsertDto itemInsertDto);
	Long updateItem(ItemUpdateDto itemUpdateDto);
	Long preSaleItem(Long itemId);
	Long onSaleItem(Long itemId);
	Long endOfSaleItem(Long itemId);
	ItemResponseDto findItemById(Long itemId);
	List<ItemResponseDto> findAllItemById(List<Long> itemIds);
}
