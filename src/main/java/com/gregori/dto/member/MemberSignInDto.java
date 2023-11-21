package com.gregori.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class MemberSignInDto {
	/**
	 * Description: 8~15자 비밀번호(대소문자 2개 이상, 숫자 1개 이상, 특수 문자 1개 이상, 공백 비허용)
	 * Matches: &test*81, te$tPa55word, testpass(7
	 * Non-Matches: mypassword, pass%5, test5324, 374833e**
	 */
	private final String REGEX_PASSWORD = "^(?=(.*[a-zA-Z].*){2,})(?=.*\\d.*)(?=.*\\W.*)[a-zA-Z0-9\\S]{8,15}$";

	@NotEmpty(message = "email은 필수값입니다.")
	@Email(message = "email 형식과 일치해야 합니다.")
	private String email;

	@NotEmpty(message = "password는 필수값입니다.")
	@Pattern(regexp = REGEX_PASSWORD, message = "password 형식이 일치해야 합니다.")
	private String password;

	public MemberSignInDto(String email, String password) {
		this.email = email;
		this.password = password;
	}
}