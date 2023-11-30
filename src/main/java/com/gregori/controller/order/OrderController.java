package com.gregori.controller.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gregori.dto.order.OrderRequestDto;
import com.gregori.dto.order.OrderResponseDto;
import com.gregori.service.order.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<String> createOrder(@RequestBody @Valid OrderRequestDto orderRequestDto) {
		orderService.createOrder(orderRequestDto);

		return ResponseEntity.status(HttpStatus.OK).body("주문을 성공했습니다.");
	}


	@GetMapping
	public ResponseEntity<OrderResponseDto> findOrderById(@PathVariable Long orderId) {
		OrderResponseDto orderResponseDto = orderService.findOrderById(orderId);

		return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
	}
}
