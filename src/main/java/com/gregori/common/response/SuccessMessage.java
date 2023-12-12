package com.gregori.common.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum
SuccessMessage {
	REGISTER_SUCCESS(HttpStatus.OK, "가입을 성공했습니다."),
	SIGNIN_SUCCESS(HttpStatus.OK, "로그인을 성공했습니다."),
	SIGNOUT_SUCCESS(HttpStatus.OK, "로그아웃을 성공했습니다."),
	CREATE_SUCCESS(HttpStatus.OK, "생성을 성공했습니다"),
	UPDATE_SUCCESS(HttpStatus.OK, "수정를 성공했습니다."),
	DELETE_SUCCESS(HttpStatus.OK, "삭제를 성공했습니다."),
	GET_SUCCESS(HttpStatus.OK, "단건 조회를 성공했습니다."),
	GET_LIST_SUCCESS(HttpStatus.OK, "목록 조회를 성공했습니다.");

	private final HttpStatus httpStatus;
	private final String description;
}
