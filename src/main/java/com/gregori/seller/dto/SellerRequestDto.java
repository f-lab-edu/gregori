package com.gregori.seller.dto;

import com.gregori.seller.domain.Seller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.gregori.common.RegexPatterns.BUSINESS_NO_REGEX;

@Getter
@AllArgsConstructor
public class SellerRequestDto {
	@NotNull
	private Long memberId;

	@NotBlank
	@Pattern(regexp = BUSINESS_NO_REGEX, message = "사업자 등록번호의 형식이 올바르지 않습니다.")
	private String businessNo;

	@NotBlank
	private String businessName;

	public Seller toEntity() {
		return Seller.builder()
			.memberId(memberId)
			.businessNo(businessNo)
			.businessName(businessName)
			.build();
	}
}
