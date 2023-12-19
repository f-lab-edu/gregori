package com.gregori.seller.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.gregori.seller.domain.Seller;

@Mapper
public interface SellerMapper {

	Long insert(Seller seller);
	Long update(Seller seller);
	void deleteByIds(List<Long> id);
	Optional<Seller> findById(Long sellerId);
	List<Seller> findByMemberId(Long memberId);
}
