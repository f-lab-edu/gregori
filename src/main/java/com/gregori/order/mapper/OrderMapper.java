package com.gregori.order.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.gregori.order.domain.Order;

@Mapper
public interface OrderMapper {

	Long insert(Order order);
	void deleteByIds(List<Long> orderIds);
	Optional<Order> findById(Long orderId);
}
