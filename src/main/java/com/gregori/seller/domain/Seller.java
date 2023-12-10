package com.gregori.seller.domain;

import com.gregori.common.AbstractEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Seller extends AbstractEntity {
	private Long id;
	private Long memberId;
	private String businessNo;
	private String businessName;

	@Builder
	public Seller(Long memberId, String businessNo, String businessName) {
		this.memberId = memberId;
		this.businessNo = businessNo;
		this.businessName = businessName;
	}

	public void updateSellerInfo(String businessNo, String businessName) {
		this.businessNo = businessNo;
		this.businessName = businessName;
	}
}
