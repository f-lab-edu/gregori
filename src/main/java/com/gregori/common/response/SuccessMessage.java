package com.gregori.common.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessMessage {

	REGISTER("가입을 성공했습니다."),
	SIGNIN("로그인을 성공했습니다."),
	SIGNOUT("로그아웃을 성공했습니다."),
	CREATE("생성을 성공했습니다"),
	UPDATE("수정를 성공했습니다."),
	DELETE("삭제를 성공했습니다."),
	GET("단건 조회를 성공했습니다."),
	GET_LIST("목록 조회를 성공했습니다.");

	private final String description;
}
