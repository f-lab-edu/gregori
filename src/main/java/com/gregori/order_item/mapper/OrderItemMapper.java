package com.gregori.order_item.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gregori.order_item.domain.OrderItem;

@Mapper
public interface OrderItemMapper {
	Long insert(OrderItem orderItem);
	void deleteByIds(List<Long> orderItemIds);
	List<OrderItem> findByOrderId(Long orderId);
	List<OrderItem> findByItemId(Long itemId);
	List<OrderItem> findByIds(List<Long> orderItemIds);
}
