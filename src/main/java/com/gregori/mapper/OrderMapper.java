package com.gregori.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.gregori.domain.order.Order;

@Mapper
public interface OrderMapper {
	Long insert(Order order);
	Optional<Order> findById(Long orderId);
}
