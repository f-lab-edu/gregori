package com.gregori.order.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.dto.OrderResponseDto;
import com.gregori.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<Void> createOrder(@RequestBody @Valid OrderRequestDto dto) {

		Long orderId = orderService.saveOrder(dto);

		return ResponseEntity.created(URI.create("/order/" + orderId)).build();
	}

	@PatchMapping("/{orderId}")
	public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {

		// TODO: memberId 변경
		orderService.cancelOrder(0L, orderId);

		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/detail/{orderDetailId}")
	public ResponseEntity<Void> cancelOrderDetail(@PathVariable Long orderDetailId) {

		orderService.cancelOrderDetail(orderDetailId);

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {

		OrderResponseDto response = orderService.getOrder(orderId);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping
	public ResponseEntity<List<OrderResponseDto>> getOrders(@RequestParam(defaultValue = "1") int page) {

		// TODO: memberId 변경
		List<OrderResponseDto> response = orderService.getOrders(0L, page);

		return ResponseEntity.ok().body(response);
	}
}
