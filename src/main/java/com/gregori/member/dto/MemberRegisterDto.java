package com.gregori.member.dto;

import static com.gregori.common.RegexPatterns.NAME_REGEX;
import static com.gregori.common.RegexPatterns.PASSWORD_REGEX;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.gregori.member.domain.Member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRegisterDto {
	@NotEmpty
	@Pattern(regexp = NAME_REGEX, message = "name 형식이 일치해야 합니다.")
	private String name;

	@NotEmpty
	@Email(message = "email 형식과 일치해야 합니다.")
	private String email;

	@NotEmpty
	@Pattern(regexp = PASSWORD_REGEX, message = "password 형식이 일치해야 합니다.")
	private String password;

	public Member toEntity(PasswordEncoder passwordEncoder) {
		return Member.builder()
			.name(name)
			.email(email)
			.password(passwordEncoder.encode(password))
			.build();
	}
}
