package com.gregori.dto;

public class RegexPatterns {
	/**
	 * Description: 2~10자 한글 이름
	 * Matches: 김밥, 갑을병정무기경신임계
	 * Non-Matches: KimChulsu, 김, 김철수1
	 */
	public static final String NAME_REGEX = "^[가-힣]{2,10}$";

	/**
	 * Description: 8~15자 비밀번호(대소문자 2개 이상, 숫자 1개 이상, 특수 문자 1개 이상, 공백 비허용)
	 * Matches: &test*81, te$tPa55word, testpass(7
	 * Non-Matches: mypassword, pass%5, test5324, 374833e**
	 */
	public static final String PASSWORD_REGEX = "^(?=(.*[a-zA-Z].*){2,})(?=.*\\d.*)(?=.*\\W.*)[a-zA-Z0-9\\S]{8,15}$";
}
