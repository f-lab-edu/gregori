package com.gregori.seller.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.gregori.common.domain.IsDeleted;
import com.gregori.seller.domain.Seller;

@Mapper
public interface SellerMapper {

	Long insert(Seller seller);
	void update(Seller seller);
	void updateIsDeleted(Long id, IsDeleted isDeleted);
	void deleteByIds(List<Long> ids);
	Optional<Seller> findById(Long id);
	List<Seller> findByMemberId(Long memberId, Integer limit, Integer offset);
}
