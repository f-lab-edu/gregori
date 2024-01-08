package com.gregori.common;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import static org.springframework.boot.web.server.Cookie.SameSite.NONE;

@Component
public class CookieGenerator {

	public static final String COOKIE_NAME = "JSESSIONID";

	public static ResponseCookie createLogoutCookie() {

		return ResponseCookie.from(COOKIE_NAME, "")
			.httpOnly(true)
			.secure(true)
			.path("/")
			.sameSite(NONE.attributeValue())
			.maxAge(0)
			.build();
	}
}
