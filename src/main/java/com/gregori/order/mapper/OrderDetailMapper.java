package com.gregori.order.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.gregori.order.domain.OrderDetail;

@Mapper
public interface OrderDetailMapper {

	Long insert(OrderDetail orderDetail);
	void updateStatus(Long id, OrderDetail.Status status);
	void deleteByIds(List<Long> ids);
	Optional<OrderDetail> findById(Long id);
	List<OrderDetail> findByOrderId(Long orderId);
	List<OrderDetail> findByProductId(Long productId);
}
