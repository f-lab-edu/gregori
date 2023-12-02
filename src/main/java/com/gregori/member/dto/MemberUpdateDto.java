package com.gregori.member.dto;


import static com.gregori.common.RegexPatterns.NAME_REGEX;
import static com.gregori.common.RegexPatterns.PASSWORD_REGEX;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberUpdateDto {
	@NotNull
	private Long id;

	@NotEmpty(message = "이름은 필수값입니다.")
	@Pattern(regexp = NAME_REGEX, message = "이름 형식이 일치해야 합니다.")
	private String name;

	@NotEmpty(message = "비밀번호는 필수값입니다.")
	@Pattern(regexp = PASSWORD_REGEX, message = "비밀번호 형식이 일치해야 합니다.")
	private String password;
}
