package com.gregori.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.gregori.domain.item.Item;

@Mapper
public interface ItemMapper {
	Long insert(Item item);
	Long update(Item item);
	Optional<Item> findById(Long itemId);
	List<Item> findAllById(List<Long> itemIds);
}
