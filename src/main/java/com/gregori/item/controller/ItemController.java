package com.gregori.item.controller;

import static com.gregori.common.response.SuccessMessage.GET_ITEM_SUCCESS;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gregori.common.response.CustomResponse;
import com.gregori.item.dto.ItemResponseDto;
import com.gregori.item.service.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {
	private final ItemService itemService;

	@GetMapping("/{itemId}")
	public ResponseEntity<CustomResponse<ItemResponseDto>> getItem(@PathVariable Long itemId) {
		CustomResponse<ItemResponseDto> response = CustomResponse
			.success(itemService.getItem(itemId), GET_ITEM_SUCCESS);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
