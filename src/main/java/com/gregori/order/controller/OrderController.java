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

import com.gregori.auth.domain.CurrentMember;
import com.gregori.auth.domain.LoginCheck;
import com.gregori.member.domain.SessionMember;
import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.dto.OrderResponseDto;
import com.gregori.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static com.gregori.auth.domain.Authority.GENERAL_MEMBER;
import static com.gregori.auth.domain.Authority.SELLING_MEMBER;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {


	private final OrderService orderService;

	@LoginCheck
	@PostMapping
	public ResponseEntity<Void> createOrder(@RequestBody @Valid OrderRequestDto dto) {

		Long orderId = orderService.saveOrder(dto);

		return ResponseEntity.created(URI.create("/order/" + orderId)).build();
	}

	@LoginCheck
	@PatchMapping("/{orderId}")
	public ResponseEntity<Void> cancelOrder(
		@CurrentMember SessionMember sessionMember, @PathVariable Long orderId) {

		orderService.cancelOrder(sessionMember.getId(), orderId);

		return ResponseEntity.noContent().build();
	}

	@LoginCheck({ GENERAL_MEMBER, SELLING_MEMBER })
	@PatchMapping("/detail/{orderDetailId}")
	public ResponseEntity<Void> cancelOrderDetail(@PathVariable Long orderDetailId) {

		orderService.cancelOrderDetail(orderDetailId);

		return ResponseEntity.noContent().build();
	}

	@LoginCheck
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {

		OrderResponseDto response = orderService.getOrder(orderId);

		return ResponseEntity.ok().body(response);
	}

	@LoginCheck
	@GetMapping
	public ResponseEntity<List<OrderResponseDto>> getOrders(
		@CurrentMember SessionMember sessionMember, @RequestParam(defaultValue = "1") int page) {

		List<OrderResponseDto> response = orderService.getOrders(sessionMember.getId(), page);

		return ResponseEntity.ok().body(response);
	}
}
