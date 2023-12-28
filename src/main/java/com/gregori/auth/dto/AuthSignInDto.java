package com.gregori.auth.dto;

import static com.gregori.common.RegexPatterns.PASSWORD_REGEX;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthSignInDto {

	@NotBlank
	@Email(message = "email 형식과 일치해야 합니다.")
	private String email;

	@NotBlank
	@Pattern(regexp = PASSWORD_REGEX, message = "password 형식이 일치해야 합니다.")
	private String password;
}
