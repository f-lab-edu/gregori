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
	private String name;
	private String email;
	private String password;
	private String businessNo;
	private String businessName;
	private Status status;

	@Getter
	@RequiredArgsConstructor
	enum Status {
		ACTIVATE("활성화"), DEACTIVATE("비활성화");
		private final String description;
	}

	@Builder
	public Seller(String name, String email, String password, String businessNo, String businessName) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.businessNo = businessNo;
		this.businessName = businessName;
		this.status = Status.ACTIVATE;
	}

	public void updateSellerInfo(String name, String password, String businessName) {
		this.name = name;
		this.password = password;
		this.businessName = businessName;
	}

	public void activate() {
		this.status = Status.ACTIVATE;
	}

	public void deactivate() {
		this.status = Status.DEACTIVATE;
	}
}
