package com.gregori.order_detail.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gregori.order_detail.domain.OrderDetail;

@Mapper
public interface OrderDetailMapper {
	Long insert(OrderDetail orderDetail);
	void deleteByIds(List<Long> orderDetailIds);
	List<OrderDetail> findByOrderId(Long orderId);
	List<OrderDetail> findByProductId(Long productId);
	List<OrderDetail> findByIds(List<Long> orderDetailIds);
}
