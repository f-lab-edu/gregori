package com.gregori.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gregori.common.response.CustomResponse;
import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.dto.OrderResponseDto;
import com.gregori.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static com.gregori.common.response.SuccessMessage.CREATE_SUCCESS;
import static com.gregori.common.response.SuccessMessage.GET_SUCCESS;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<CustomResponse<OrderResponseDto>> createOrder(@RequestBody @Valid OrderRequestDto orderRequestDto) {
		CustomResponse<OrderResponseDto> response = CustomResponse
			.success(orderService.saveOrder(orderRequestDto), CREATE_SUCCESS);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<CustomResponse<OrderResponseDto>> getOrder(@PathVariable Long orderId) {
		CustomResponse<OrderResponseDto> response = CustomResponse
			.success(orderService.getOrder(orderId), GET_SUCCESS);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
