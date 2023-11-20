package com.gregori.controller.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
// @NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberSignUpDto {
	/**
	 * Description: 2~10자 한글 이름
	 * Matches: 김밥, 갑을병정무기경신임계
	 * Non-Matches: KimChulsu, 김, 김철수1
	 */
	private final String REGEX_NAME = "^[가-힣]{2,10}$";
	/**
	 * Description: 8~15자 비밀번호(대소문자 2개 이상, 숫자 1개 이상, 특수 문자 1개 이상, 공백 비허용)
	 * Matches: &test*81, te$tPa55word, testpass(7
	 * Non-Matches: mypassword, pass%5, test5324, 374833e**
	 */
	private final String REGEX_PASSWORD = "^(?=(.*[a-zA-Z].*){2,})(?=.*\\d.*)(?=.*\\W.*)[a-zA-Z0-9\\S]{8,15}$";

	@NotEmpty(message = "name은 필수값입니다.")
	@Pattern(regexp = REGEX_NAME, message = "name 형식이 일치해야 합니다.")
	private String name;

	@NotEmpty(message = "email은 필수값입니다.")
	@Email(message = "email 형식과 일치해야 합니다.")
	private String email;

	@NotEmpty(message = "password는 필수값입니다.")
	@Pattern(regexp = REGEX_PASSWORD, message = "password 형식이 일치해야 합니다.")
	private String password;

	public MemberSignUpDto(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}
}
