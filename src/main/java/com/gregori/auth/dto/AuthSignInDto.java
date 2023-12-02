package com.gregori.auth.dto;

import static com.gregori.common.RegexPatterns.PASSWORD_REGEX;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.gregori.refresh_token.domain.RefreshToken;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthSignInDto {
	@NotEmpty(message = "email은 필수값입니다.")
	@Email(message = "email 형식과 일치해야 합니다.")
	private String email;

	@NotEmpty(message = "password는 필수값입니다.")
	@Pattern(regexp = PASSWORD_REGEX, message = "password 형식이 일치해야 합니다.")
	private String password;

	public UsernamePasswordAuthenticationToken toAuthentication() {
		return new UsernamePasswordAuthenticationToken(email, password);
	}

	public RefreshToken toEntity(String name, String token) {
		return RefreshToken.builder()
			.refreshTokenKey(name)
			.refreshTokenValue(token)
			.build();
	}
}
