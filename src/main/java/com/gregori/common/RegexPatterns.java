package com.gregori.common;

public class RegexPatterns {
	public static final String NAME_REGEX = "^[가-힣]{2,10}$";
	public static final String PASSWORD_REGEX = "^(?=(.*[a-zA-Z].*){2,})(?=.*\\d.*)(?=.*\\W.*)[a-zA-Z0-9\\S]{8,15}$";
	public static final String BUSINESS_NO_REGEX = "^\\d{3}-\\d{2}-\\d{5}$";
}
