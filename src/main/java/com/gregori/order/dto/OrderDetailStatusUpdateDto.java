package com.gregori.order.dto;

import com.gregori.order.domain.OrderDetail;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailStatusUpdateDto {

	@NotNull
	private Long orderDetailId;

	@NotBlank
	private OrderDetail.Status status;
}
