package com.gregori.common.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessMessage {
	REGISTER_SUCCESS(HttpStatus.OK, "회원 가입에 성공했습니다."),
	SIGNIN_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다."),
	SIGNOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공했습니다."),
	UPDATE_MEMBER_SUCCESS(HttpStatus.OK, "회원 수정에 성공했습니다."),
	DEACTIVATE_MEMBER_SUCCESS(HttpStatus.OK, "회원 수정에 성공했습니다."),
	FIND_MEMBER_SUCCESS(HttpStatus.OK, "회원 정보 찾기에 성공했습니다.");

	private final HttpStatus httpStatus;
	private final String description;
}
