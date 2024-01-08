package com.gregori.member.dto;


import static com.gregori.common.RegexPatterns.NAME_REGEX;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberNameUpdateDto {

	@NotBlank
	@Pattern(regexp = NAME_REGEX, message = "이름 형식이 일치해야 합니다.")
	private String name;
}
