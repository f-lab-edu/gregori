package com.gregori.order.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.gregori.order.domain.Order;

@Mapper
public interface OrderMapper {

	Long insert(Order order);
	void updateStatus(Long id, Order.Status status);
	void deleteByIds(List<Long> ids);
	Optional<Order> findById(Long id);
	List<Order> findByMemberId(Long memberId, Integer limit, Integer offset);
}
