package com.gregori.item.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.gregori.item.domain.Item;

@Mapper
public interface ItemMapper {
	Long insert(Item item);
	Long update(Item item);
	Long deleteByIds(List<Long> itemIds);
	Optional<Item> findById(Long itemId);
	List<Item> findAllById(List<Long> itemIds);
}
