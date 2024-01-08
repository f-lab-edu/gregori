package com.gregori.seller.domain;

import com.gregori.common.AbstractEntity;
import com.gregori.common.domain.IsDeleted;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.gregori.common.domain.IsDeleted.FALSE;
import static com.gregori.common.domain.IsDeleted.TRUE;

@Getter
@NoArgsConstructor
public class Seller extends AbstractEntity {

	private Long id;
	private Long memberId;
	private String businessNumber;
	private String businessName;
	private IsDeleted isDeleted;

	@Builder
	public Seller(Long memberId, String businessNumber, String businessName) {

		this.memberId = memberId;
		this.businessNumber = businessNumber;
		this.businessName = businessName;
		this.isDeleted = FALSE;
	}

	public void updateSellerInfo(String businessNumber, String businessName) {

		this.businessNumber = businessNumber;
		this.businessName = businessName;
	}

	public void isDeletedTrue() {
		this.isDeleted = TRUE;
	}
}
