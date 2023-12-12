package com.gregori.seller.dto;

import com.gregori.seller.domain.Seller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerResponseDto {
	private Long id;
	private Long memberId;
	private String businessNumber;
	private String businessName;

	public SellerResponseDto toEntity(Seller seller) {
		return SellerResponseDto.builder()
			.id(seller.getId())
			.memberId(seller.getMemberId())
			.businessNumber(seller.getBusinessNumber())
			.businessName(seller.getBusinessName())
			.build();
	}
}
