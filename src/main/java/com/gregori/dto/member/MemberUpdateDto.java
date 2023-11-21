package com.gregori.dto.member;


import static com.gregori.dto.RegexPatterns.*;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class MemberUpdateDto {

	@NotNull
	private Long id;

	@NotEmpty(message = "이름은 필수값입니다.")
	@Pattern(regexp = NAME_REGEX, message = "이름 형식이 일치해야 합니다.")
	private String name;

	@NotEmpty(message = "비밀번호는 필수값입니다.")
	@Pattern(regexp = PASSWORD_REGEX, message = "비밀번호 형식이 일치해야 합니다.")
	private String password;

	public MemberUpdateDto(Long id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = password;
	}
}
