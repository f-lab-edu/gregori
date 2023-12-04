package com.gregori.order.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.gregori.order.domain.Order;

@Mapper
public interface OrderMapper {
	Long insert(Order order);
	Optional<Order> findById(Long orderId);
}
