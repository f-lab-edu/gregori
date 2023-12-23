package com.gregori.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.gregori.common.RegexPatterns.PASSWORD_REGEX;

@Getter
@AllArgsConstructor
public class MemberPasswordUpdateDto {

	@NotNull
	private Long id;

	@NotBlank
	@Pattern(regexp = PASSWORD_REGEX, message = "비밀번호 형식이 일치해야 합니다.")
	private String oldPassword;

	@NotBlank
	@Pattern(regexp = PASSWORD_REGEX, message = "비밀번호 형식이 일치해야 합니다.")
	private String newPassword;
}
