package com.gregori.member.dto;

import static com.gregori.common.RegexPatterns.NAME_REGEX;
import static com.gregori.common.RegexPatterns.PASSWORD_REGEX;

import com.gregori.member.domain.Member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberRegisterDto {

	@NotBlank
	@Pattern(regexp = NAME_REGEX, message = "이름 형식이 올바르지 않습니다.")
	private String name;

	@NotBlank
	@Email(message = "이메일 형식이 올바르지 않습니다.")
	private String email;

	@NotBlank
	@Pattern(regexp = PASSWORD_REGEX, message = "비밀번호 형식이 올바르지 않습니다.")
	private String password;

	public Member toEntity(String encodedPassword) {

		return Member.builder()
			.name(name)
			.email(email)
			.password(encodedPassword)
			.build();
	}
}
