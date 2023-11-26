package com.gregori.dto.auth;

import static com.gregori.common.RegexPatterns.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class AuthSignInDto {
	@NotEmpty(message = "email은 필수값입니다.")
	@Email(message = "email 형식과 일치해야 합니다.")
	private String email;

	@NotEmpty(message = "password는 필수값입니다.")
	@Pattern(regexp = PASSWORD_REGEX, message = "password 형식이 일치해야 합니다.")
	private String password;

	public AuthSignInDto(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
