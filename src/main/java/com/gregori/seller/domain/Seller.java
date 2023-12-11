package com.gregori.seller.domain;

import com.gregori.common.AbstractEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class Seller extends AbstractEntity {
	private Long id;
	private Long memberId;
	private String businessNo;
	private String businessName;
	private Status status;

	@Getter
	@RequiredArgsConstructor
	public enum Status {
		OPERATING("영업"), CLOSED("폐업");
		private final String description;
	}

	@Builder
	public Seller(Long memberId, String businessNo, String businessName) {
		this.memberId = memberId;
		this.businessNo = businessNo;
		this.businessName = businessName;
		this.status = Status.OPERATING;
	}

	public void updateSellerInfo(String businessNo, String businessName) {
		this.businessNo = businessNo;
		this.businessName = businessName;
	}

	public void operating() {
		this.status = Status.OPERATING;
	}

	public void closed() {
		this.status = Status.CLOSED;
	}
}
