package com.gregori.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRequestDto {
	@NotBlank
	private String accessToken;

	@NotBlank
	private String refreshToken;
}
