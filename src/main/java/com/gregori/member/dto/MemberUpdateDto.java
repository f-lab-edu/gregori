package com.gregori.member.dto;


import static com.gregori.common.RegexPatterns.NAME_REGEX;
import static com.gregori.common.RegexPatterns.PASSWORD_REGEX;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberUpdateDto {

	@NotNull
	private Long id;

	@NotBlank
	@Pattern(regexp = NAME_REGEX, message = "이름 형식이 일치해야 합니다.")
	private String name;

	@NotBlank
	@Pattern(regexp = PASSWORD_REGEX, message = "비밀번호 형식이 일치해야 합니다.")
	private String password;
}
