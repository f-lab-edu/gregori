package com.gregori.controller.item;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gregori.dto.item.ItemResponseDto;
import com.gregori.service.item.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
	private final ItemService itemService;

	@GetMapping("/{itemId}")
	public ResponseEntity<ItemResponseDto> findItemById(@PathVariable Long itemId) {
		ItemResponseDto itemResponseDto = itemService.findItemById(itemId);

		return ResponseEntity.status(HttpStatus.OK).body(itemResponseDto);
	}
}
